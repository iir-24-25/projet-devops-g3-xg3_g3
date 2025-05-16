import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Alert,
  Linking,
  Modal,
  SafeAreaView,
  Platform,
} from 'react-native';
import { WebView } from 'react-native-webview';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useAssignments } from '../context/AssignmentContext';
import { useGroups } from '../context/GroupContext';
import { useAuth } from '../context/AuthContext';
import * as FileSystem from 'expo-file-system';
import * as Sharing from 'expo-sharing';

type AssignmentDetailsScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'AssignmentDetails'>;
type AssignmentDetailsScreenRouteProp = RouteProp<RootStackParamList, 'AssignmentDetails'>;

const AssignmentDetailsScreen = () => {
  const navigation = useNavigation<AssignmentDetailsScreenNavigationProp>();
  const route = useRoute<AssignmentDetailsScreenRouteProp>();
  const { assignmentId } = route.params;
  const { getAssignmentById, deleteAssignment, updateAssignment } = useAssignments();
  const { groups = [] } = useGroups();
  const { user } = useAuth();
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);
  const [showFileViewer, setShowFileViewer] = useState(false);

  const assignment = getAssignmentById(assignmentId);

  if (!assignment) {
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity
            style={styles.backButton}
            onPress={() => navigation.goBack()}
          >
            <Text style={styles.backButtonText}>←</Text>
          </TouchableOpacity>
          <Text style={styles.headerTitle}>Assignment Details</Text>
        </View>
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>Assignment not found</Text>
        </View>
      </View>
    );
  }

  console.log('Current user:', user);
  console.log('User role:', user?.role);
  console.log('Assignment:', assignment);

  const group = groups.find(g => g.id === assignment.groupId);
  const canEdit = user?.role === 'admin' || user?.role === 'teacher';

  console.log('Can edit:', canEdit);

  const handleEdit = () => {
    navigation.navigate('EditAssignment', { assignmentId: assignment.id });
  };

  const handleDelete = () => {
    setIsDeleteModalVisible(true);
  };

  const confirmDelete = () => {
    deleteAssignment(assignment.id);
    setIsDeleteModalVisible(false);
    navigation.goBack();
  };

  const handleViewFile = async () => {
    if (assignment.fileUrl) {
      try {
        if (Platform.OS === 'web') {
          // For web platform, use direct download
          const link = document.createElement('a');
          link.href = assignment.fileUrl;
          link.download = assignment.fileName;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        } else {
          // For mobile platforms, use expo-file-system
          const fileUri = FileSystem.documentDirectory + assignment.fileName;
          
          // Download the file
          const downloadResult = await FileSystem.downloadAsync(
            assignment.fileUrl,
            fileUri
          );

          if (downloadResult.status === 200) {
            // Share the downloaded file
            await Sharing.shareAsync(fileUri);
          } else {
            Alert.alert('Error', 'Failed to download file');
          }
        }
      } catch (error) {
        console.error('Error downloading file:', error);
        Alert.alert('Error', 'Failed to download file');
      }
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Assignment Details</Text>
        {canEdit && (
          <View style={styles.headerButtons}>
            <TouchableOpacity
              style={[styles.headerButton, styles.editButton]}
              onPress={handleEdit}
              accessibilityRole="button"
              accessibilityLabel="Edit assignment"
            >
              <Text style={styles.headerButtonText}>Edit</Text>
            </TouchableOpacity>
          </View>
        )}
      </View>

      <ScrollView style={styles.content}>
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Description</Text>
          <Text style={styles.description}>{assignment.description}</Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Due Date</Text>
          <Text style={styles.dueDate}>
            {new Date(assignment.dueDate).toLocaleDateString()}
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Group</Text>
          <Text style={styles.detailValue}>{group?.name || 'Unknown Group'}</Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Status</Text>
          <Text style={styles.detailValue}>{assignment.status}</Text>
        </View>

        {assignment.fileUrl && (
          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Download File</Text>
            <TouchableOpacity 
              style={styles.fileButton}
              onPress={handleViewFile}
              accessibilityRole="button"
              accessibilityLabel={`Download file ${assignment.fileName}`}
            >
              <Text style={styles.fileButtonText}>Download {assignment.fileName}</Text>
            </TouchableOpacity>
          </View>
        )}

        {canEdit && (
          <View style={styles.actionButtons}>
            <TouchableOpacity 
              style={[styles.actionButton, styles.deleteButton]}
              onPress={handleDelete}
              accessibilityRole="button"
              accessibilityLabel="Delete assignment"
            >
              <Text style={styles.actionButtonText}>Delete</Text>
            </TouchableOpacity>
          </View>
        )}
      </ScrollView>

      <Modal
        visible={isDeleteModalVisible}
        transparent
        animationType="fade"
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Delete Assignment</Text>
            <Text style={styles.modalText}>
              Are you sure you want to delete this assignment? This action cannot be undone.
            </Text>
            <View style={styles.modalButtons}>
              <TouchableOpacity
                style={[styles.modalButton, styles.cancelButton]}
                onPress={() => setIsDeleteModalVisible(false)}
              >
                <Text style={styles.modalButtonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.modalButton, styles.confirmButton]}
                onPress={confirmDelete}
              >
                <Text style={styles.modalButtonText}>Delete</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  backButton: {
    marginRight: 16,
  },
  backButtonText: {
    fontSize: 24,
    color: '#1a73e8',
  },
  headerTitle: {
    flex: 1,
    fontSize: 20,
    fontWeight: 'bold',
    color: '#202124',
  },
  headerButtons: {
    flexDirection: 'row',
    gap: 8,
  },
  headerButton: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 4,
  },
  editButton: {
    backgroundColor: '#1a73e8',
  },
  headerButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '500',
  },
  content: {
    flex: 1,
    padding: 16,
  },
  section: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#202124',
    marginBottom: 12,
  },
  description: {
    fontSize: 16,
    color: '#5f6368',
    lineHeight: 24,
  },
  dueDate: {
    fontSize: 16,
    color: '#5f6368',
  },
  detailValue: {
    flex: 1,
    fontSize: 16,
    color: '#202124',
  },
  fileButton: {
    backgroundColor: '#1a73e8',
    padding: 12,
    borderRadius: 4,
    alignItems: 'center',
  },
  fileButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
  actionButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 16,
  },
  actionButton: {
    flex: 1,
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginHorizontal: 8,
  },
  deleteButton: {
    backgroundColor: '#db4437',
  },
  actionButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContent: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 24,
    width: '80%',
    maxWidth: 400,
    height: '80%',
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#202124',
    marginBottom: 16,
  },
  modalText: {
    fontSize: 16,
    color: '#5f6368',
    marginBottom: 24,
    lineHeight: 24,
  },
  modalButtons: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
  },
  modalButton: {
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 4,
    marginLeft: 8,
  },
  cancelButton: {
    backgroundColor: '#f1f3f4',
  },
  confirmButton: {
    backgroundColor: '#db4437',
  },
  modalButtonText: {
    fontSize: 14,
    fontWeight: '500',
    color: '#202124',
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  errorText: {
    fontSize: 16,
    color: '#db4437',
    textAlign: 'center',
  },
});

export default AssignmentDetailsScreen; 
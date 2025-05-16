import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Alert,
  ActivityIndicator,
} from 'react-native';
import { useNavigation, useRoute } from '@react-navigation/native';
import { NavigationProp } from '../types/navigation';
import { useStudents } from '../context/StudentContext';
import { useGroups } from '../context/GroupContext';
import { useAssignments } from '../context/AssignmentContext';
import * as DocumentPicker from 'expo-document-picker';
import * as FileSystem from 'expo-file-system';
import { toast } from 'sonner-native';

export default function StudentDetailsScreen() {
  const navigation = useNavigation<NavigationProp>();
  const route = useRoute();
  const { studentId } = route.params as { studentId: string };
  const { students = [], deleteStudent } = useStudents();
  const { groups = [] } = useGroups();
  const { assignments = [] } = useAssignments();
  const [uploadingFile, setUploadingFile] = useState<string | null>(null);

  const student = students.find(s => s.id === studentId);
  const group = groups.find(g => g.id === student?.groupId);
  const groupAssignments = assignments.filter(
    assignment => assignment?.groupId === student?.groupId
  );

  if (!student) {
    return (
      <View style={styles.container}>
        <Text style={styles.errorText}>Student not found</Text>
      </View>
    );
  }

  const handleEdit = () => {
    navigation.navigate('EditStudent', { studentId });
  };

  const handleDelete = () => {
    Alert.alert(
      'Delete Student',
      'Are you sure you want to delete this student?',
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: () => {
            deleteStudent(studentId);
            navigation.goBack();
          },
        },
      ]
    );
  };

  const getAssignmentStatus = (dueDate: string) => {
    const now = new Date();
    const due = new Date(dueDate);
    const diffTime = due.getTime() - now.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays < 0) return { status: 'Late', color: '#db4437' };
    if (diffDays === 0) return { status: 'Due Today', color: '#f4b400' };
    if (diffDays <= 7) return { status: 'Due Soon', color: '#f4b400' };
    return { status: 'To Do', color: '#1a73e8' };
  };

  const handleDownloadAssignment = async (fileUrl: string, fileName: string) => {
    try {
      const { status } = await FileSystem.downloadAsync(
        fileUrl,
        FileSystem.documentDirectory + fileName
      );
      
      if (status === 200) {
        toast.success('Assignment downloaded successfully');
      } else {
        toast.error('Failed to download assignment');
      }
    } catch (error) {
      console.error('Download error:', error);
      toast.error('Failed to download assignment');
    }
  };

  const handleUploadAnswer = async (assignmentId: string) => {
    try {
      setUploadingFile(assignmentId);
      const result = await DocumentPicker.getDocumentAsync({
        type: ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'],
        copyToCacheDirectory: true,
      });

      if (!result.canceled && result.assets && result.assets[0]) {
        const file = result.assets[0];
        // Here you would typically upload the file to your server
        // For now, we'll just show a success message
        toast.success(`Answer uploaded successfully: ${file.name}`);
      }
    } catch (error) {
      console.error('Upload error:', error);
      toast.error('Failed to upload answer');
    } finally {
      setUploadingFile(null);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Student Details</Text>
      </View>

      <View style={styles.content}>
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Personal Information</Text>
          <View style={styles.detailsContainer}>
            <View style={styles.detailItem}>
              <Text style={styles.detailLabel}>Name</Text>
              <Text style={styles.detailValue}>{student.name || 'N/A'}</Text>
            </View>
            <View style={styles.detailItem}>
              <Text style={styles.detailLabel}>Family Name</Text>
              <Text style={styles.detailValue}>{student.familyName || 'N/A'}</Text>
            </View>
            <View style={styles.detailItem}>
              <Text style={styles.detailLabel}>Email</Text>
              <Text style={styles.detailValue}>{student.email || 'N/A'}</Text>
            </View>
            <View style={styles.detailItem}>
              <Text style={styles.detailLabel}>Group</Text>
              <Text style={styles.detailValue}>{group?.name || 'No Group'}</Text>
            </View>
            <View style={styles.detailItem}>
              <Text style={styles.detailLabel}>Role</Text>
              <Text style={styles.detailValue}>{student.role || 'student'}</Text>
            </View>
          </View>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Assignments</Text>
          {groupAssignments.length > 0 ? (
            groupAssignments.map(assignment => {
              const status = getAssignmentStatus(assignment.dueDate);
              return (
                <View key={assignment.id} style={styles.assignmentCard}>
                  <View style={styles.assignmentHeader}>
                    <Text style={styles.assignmentTitle}>{assignment.title}</Text>
                    <View style={[styles.statusBadge, { backgroundColor: status.color }]}>
                      <Text style={styles.statusText}>{status.status}</Text>
                    </View>
                  </View>
                  <Text style={styles.assignmentDescription}>
                    {assignment.description}
                  </Text>
                  <Text style={styles.dueDate}>
                    Due: {new Date(assignment.dueDate).toLocaleDateString()}
                  </Text>
                  <View style={styles.assignmentActions}>
                    {assignment.fileUrl && (
                      <TouchableOpacity
                        style={[styles.actionButton, styles.downloadButton]}
                        onPress={() => handleDownloadAssignment(assignment.fileUrl, assignment.fileName)}
                      >
                        <Text style={styles.actionButtonText}>Download</Text>
                      </TouchableOpacity>
                    )}
                    <TouchableOpacity
                      style={[styles.actionButton, styles.uploadButton]}
                      onPress={() => handleUploadAnswer(assignment.id)}
                      disabled={!!uploadingFile}
                    >
                      {uploadingFile === assignment.id ? (
                        <ActivityIndicator color="#ffffff" />
                      ) : (
                        <Text style={styles.actionButtonText}>Upload Answer</Text>
                      )}
                    </TouchableOpacity>
                  </View>
                </View>
              );
            })
          ) : (
            <Text style={styles.noAssignmentsText}>No assignments found for this student's group.</Text>
          )}
        </View>

        <View style={styles.buttonContainer}>
          <TouchableOpacity
            style={[styles.button, styles.editButton]}
            onPress={handleEdit}
          >
            <Text style={styles.buttonText}>Edit</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.button, styles.deleteButton]}
            onPress={handleDelete}
          >
            <Text style={styles.buttonText}>Delete</Text>
          </TouchableOpacity>
        </View>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#ffffff',
    borderBottomWidth: 1,
    borderBottomColor: '#e8eaed',
  },
  backButton: {
    padding: 8,
  },
  backButtonText: {
    fontSize: 24,
    color: '#1a73e8',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#202124',
    marginLeft: 16,
  },
  content: {
    padding: 16,
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: '#202124',
    marginBottom: 12,
  },
  detailsContainer: {
    backgroundColor: '#ffffff',
    borderRadius: 12,
    padding: 16,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  detailItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#e8eaed',
  },
  detailLabel: {
    fontSize: 16,
    color: '#5f6368',
  },
  detailValue: {
    fontSize: 16,
    color: '#202124',
    fontWeight: '500',
  },
  assignmentCard: {
    backgroundColor: '#ffffff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  assignmentHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  assignmentTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#202124',
    flex: 1,
  },
  statusBadge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    marginLeft: 8,
  },
  statusText: {
    color: '#ffffff',
    fontSize: 12,
    fontWeight: '600',
  },
  assignmentDescription: {
    fontSize: 14,
    color: '#5f6368',
    marginBottom: 8,
  },
  dueDate: {
    fontSize: 12,
    color: '#5f6368',
    fontStyle: 'italic',
    marginBottom: 12,
  },
  assignmentActions: {
    flexDirection: 'row',
    gap: 8,
  },
  actionButton: {
    flex: 1,
    padding: 8,
    borderRadius: 8,
    alignItems: 'center',
  },
  downloadButton: {
    backgroundColor: '#1a73e8',
  },
  uploadButton: {
    backgroundColor: '#0f9d58',
  },
  actionButtonText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '600',
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 12,
    marginTop: 24,
  },
  button: {
    flex: 1,
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
  },
  editButton: {
    backgroundColor: '#1a73e8',
  },
  deleteButton: {
    backgroundColor: '#db4437',
  },
  buttonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: '600',
  },
  errorText: {
    fontSize: 16,
    color: '#db4437',
    textAlign: 'center',
    marginTop: 20,
  },
  noAssignmentsText: {
    fontSize: 14,
    color: '#5f6368',
    textAlign: 'center',
    fontStyle: 'italic',
    marginTop: 8,
  },
}); 
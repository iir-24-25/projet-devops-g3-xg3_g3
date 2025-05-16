import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useAssignments } from '../context/AssignmentContext';
import { useGroups } from '../context/GroupContext';

type AssignmentListScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'AssignmentList'>;

export default function AssignmentListScreen() {
  const navigation = useNavigation<AssignmentListScreenNavigationProp>();
  const { assignments, deleteAssignment } = useAssignments();
  const { groups } = useGroups();

  const getGroupName = (groupId: string) => {
    const group = groups.find(g => g.id === groupId);
    return group ? group.name : 'Unknown Group';
  };

  const handleAssignmentPress = (assignmentId: string) => {
    navigation.navigate('AssignmentDetails', { assignmentId });
  };

  const handleDeleteAssignment = async (assignmentId: string) => {
    console.log('Delete button pressed for assignment:', assignmentId);
    console.log('Current assignments list:', assignments);
    
    Alert.alert(
      'Delete Assignment',
      'Are you sure you want to delete this assignment?',
      [
        {
          text: 'Cancel',
          style: 'cancel',
          onPress: () => console.log('Delete cancelled'),
        },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: async () => {
            try {
              console.log('Delete confirmed for assignment:', assignmentId);
              await deleteAssignment(assignmentId);
              console.log('Delete function completed');
            } catch (error) {
              console.error('Error deleting assignment:', error);
              Alert.alert('Error', 'Failed to delete assignment. Please try again.');
            }
          },
        },
      ]
    );
  };

  const renderAssignmentItem = ({ item }: { item: any }) => (
    <TouchableOpacity
      style={styles.assignmentItem}
      onPress={() => handleAssignmentPress(item.id)}
    >
      <View style={styles.assignmentInfo}>
        <Text style={styles.assignmentTitle}>{item.title}</Text>
        <Text style={styles.assignmentGroup}>Group: {getGroupName(item.groupId)}</Text>
        <Text style={styles.assignmentDueDate}>
          Due: {new Date(item.dueDate).toLocaleDateString()}
        </Text>
      </View>
      <TouchableOpacity
        style={styles.deleteButton}
        onPress={() => handleDeleteAssignment(item.id)}
      >
        <Text style={styles.deleteButtonText}>Delete</Text>
      </TouchableOpacity>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Text style={styles.backButtonText}>Back</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Assignments</Text>
      </View>
      <FlatList
        data={assignments}
        renderItem={renderAssignmentItem}
        keyExtractor={item => item.id}
        contentContainerStyle={styles.list}
      />
      <TouchableOpacity
        style={styles.addButton}
        onPress={() => navigation.navigate('AddAssignment')}
      >
        <Text style={styles.addButtonText}>Add Assignment</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  list: {
    padding: 20,
  },
  assignmentItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 15,
    backgroundColor: '#f8f9fa',
    borderRadius: 10,
    marginBottom: 10,
  },
  assignmentInfo: {
    flex: 1,
  },
  assignmentTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  assignmentGroup: {
    fontSize: 14,
    color: '#666',
    marginBottom: 3,
  },
  assignmentDueDate: {
    fontSize: 14,
    color: '#666',
  },
  deleteButton: {
    backgroundColor: '#dc3545',
    padding: 8,
    borderRadius: 5,
    marginLeft: 10,
  },
  deleteButtonText: {
    color: '#fff',
    fontSize: 12,
  },
  addButton: {
    position: 'absolute',
    bottom: 20,
    right: 20,
    backgroundColor: '#007AFF',
    padding: 15,
    borderRadius: 30,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  addButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  backButton: {
    marginRight: 20,
  },
  backButtonText: {
    fontSize: 16,
    color: '#007AFF',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
  },
}); 
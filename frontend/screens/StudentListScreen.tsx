import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, FlatList, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { NavigationProp, Student } from '../types/navigation';
import { toast } from 'sonner-native';
import { useStudents } from '../context/StudentContext';

type RoleStyle = {
  rolestudent: object;
  roleteacher: object;
  roleadmin: object;
};

export default function StudentListScreen() {
  const navigation = useNavigation<NavigationProp>();
  const { students, deleteStudent } = useStudents();

  // Filter only students with role 'student'
  const studentList = students.filter(student => student.role === 'student');

  const handleBack = () => {
    navigation.goBack();
  };

  const handleDeleteStudent = (studentId: string) => {
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
          onPress: async () => {
            try {
              await deleteStudent(studentId);
              toast.success('Student deleted successfully');
            } catch (error) {
              toast.error('Failed to delete student. Please try again.');
            }
          },
        },
      ],
    );
  };

  const handleAddStudent = () => {
    navigation.navigate('AddStudent');
  };

  const handleStudentPress = (student: Student) => {
    navigation.navigate('StudentDetails', { studentId: student.id });
  };

  const getRoleDisplay = (role: string | undefined) => {
    if (!role) return 'Student';
    return role.charAt(0).toUpperCase() + role.slice(1);
  };

  const getRoleStyle = (role: string | undefined) => {
    if (!role) return styles.rolestudent;
    const roleKey = `role${role}` as keyof RoleStyle;
    return styles[roleKey] || styles.rolestudent;
  };

  const renderStudentItem = ({ item }: { item: Student }) => (
    <TouchableOpacity
      style={styles.studentCard}
      onPress={() => handleStudentPress(item)}
    >
      <View style={styles.studentInfo}>
        <Text style={styles.studentName}>
          {item.name || ''} {item.familyName || ''}
        </Text>
        <Text style={styles.studentEmail}>{item.email || ''}</Text>
        <View style={styles.studentDetails}>
          <Text style={styles.studentGroup}>{item.group || 'No Group'}</Text>
          <Text style={[styles.studentRole, getRoleStyle(item.role)]}>
            {getRoleDisplay(item.role)}
          </Text>
        </View>
      </View>
      <TouchableOpacity
        style={styles.deleteButton}
        onPress={() => handleDeleteStudent(item.id)}
      >
        <Text style={styles.deleteButtonText}>Delete</Text>
      </TouchableOpacity>
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={handleBack} style={styles.backButton}>
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Students</Text>
        <TouchableOpacity style={styles.addButton} onPress={handleAddStudent}>
          <Text style={styles.addButtonText}>Add Student</Text>
        </TouchableOpacity>
      </View>
      <FlatList
        data={studentList}
        renderItem={renderStudentItem}
        keyExtractor={item => item.id}
        contentContainerStyle={styles.list}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#ddd',
  },
  backButton: {
    padding: 8,
  },
  backButtonText: {
    fontSize: 24,
    color: '#007AFF',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    flex: 1,
    textAlign: 'center',
  },
  addButton: {
    backgroundColor: '#007AFF',
    padding: 10,
    borderRadius: 8,
  },
  addButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '600',
  },
  list: {
    padding: 20,
  },
  studentCard: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 15,
    marginBottom: 10,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 3,
  },
  studentInfo: {
    flex: 1,
  },
  studentName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 4,
  },
  studentEmail: {
    fontSize: 14,
    color: '#666',
    marginBottom: 2,
  },
  studentDetails: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  studentGroup: {
    fontSize: 14,
    color: '#007AFF',
  },
  studentRole: {
    fontSize: 12,
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 4,
    overflow: 'hidden',
  },
  rolestudent: {
    backgroundColor: '#E3F2FD',
    color: '#1976D2',
  },
  roleteacher: {
    backgroundColor: '#E8F5E9',
    color: '#2E7D32',
  },
  roleadmin: {
    backgroundColor: '#FFF3E0',
    color: '#E65100',
  },
  deleteButton: {
    backgroundColor: '#FF3B30',
    padding: 8,
    borderRadius: 6,
    marginLeft: 10,
  },
  deleteButtonText: {
    color: '#fff',
    fontSize: 12,
    fontWeight: '600',
  },
}); 
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation, useRoute } from '@react-navigation/native';
import { useGroups } from '../context/GroupContext';
import { useTeachers } from '../context/TeacherContext';
import { useStudents } from '../context/StudentContext';
import { NavigationProp, RouteProp } from '../types/navigation';

export default function GroupDetailsScreen() {
  const navigation = useNavigation<NavigationProp>();
  const route = useRoute<RouteProp<{ GroupDetails: { groupId: string } }>>();
  const { groups } = useGroups();
  const { teachers } = useTeachers();
  const { students } = useStudents();

  const group = groups.find(g => g.id === route.params.groupId);
  const teacher = teachers.find(t => t.id === group?.teacherId);
  const groupStudents = students.filter(s => group?.studentIds.includes(s.id));

  if (!group) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
            <Text style={styles.backButtonText}>←</Text>
          </TouchableOpacity>
          <Text style={styles.title}>Group Not Found</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>{group.name}</Text>
        <TouchableOpacity
          onPress={() => navigation.navigate('EditGroup', { groupId: group.id })}
          style={styles.editButton}
        >
          <Text style={styles.editButtonText}>Edit</Text>
        </TouchableOpacity>
      </View>

      <ScrollView style={styles.content}>
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Teacher</Text>
          {teacher ? (
            <View style={styles.teacherCard}>
              <Text style={styles.teacherName}>
                {teacher.name} {teacher.familyName}
              </Text>
              <Text style={styles.teacherSubject}>{teacher.subject}</Text>
            </View>
          ) : (
            <Text style={styles.noData}>No teacher assigned</Text>
          )}
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Students ({groupStudents.length})</Text>
          {groupStudents.length > 0 ? (
            groupStudents.map(student => (
              <View key={student.id} style={styles.studentCard}>
                <Text style={styles.studentName}>
                  {student.name} {student.familyName}
                </Text>
              </View>
            ))
          ) : (
            <Text style={styles.noData}>No students assigned</Text>
          )}
        </View>
      </ScrollView>
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
    borderBottomColor: '#eee',
  },
  backButton: {
    padding: 10,
  },
  backButtonText: {
    fontSize: 24,
    color: '#007AFF',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
  },
  editButton: {
    padding: 10,
  },
  editButtonText: {
    fontSize: 16,
    color: '#007AFF',
    fontWeight: '600',
  },
  content: {
    flex: 1,
    padding: 20,
  },
  section: {
    backgroundColor: '#fff',
    borderRadius: 15,
    padding: 20,
    marginBottom: 20,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 3,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginBottom: 15,
  },
  teacherCard: {
    backgroundColor: '#f8f8f8',
    borderRadius: 10,
    padding: 15,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  teacherName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 5,
  },
  teacherSubject: {
    fontSize: 14,
    color: '#666',
  },
  studentCard: {
    backgroundColor: '#f8f8f8',
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  studentName: {
    fontSize: 16,
    color: '#333',
  },
  noData: {
    fontSize: 16,
    color: '#666',
    fontStyle: 'italic',
  },
}); 
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { useGroups } from '../context/GroupContext';
import { useTeachers } from '../context/TeacherContext';
import { NavigationProp } from '../types/navigation';

export default function GroupListScreen() {
  const navigation = useNavigation<NavigationProp>();
  const { groups } = useGroups();
  const { teachers } = useTeachers();

  const renderGroupItem = (group: any) => {
    const teacher = teachers.find(t => t.id === group.teacherId);
    return (
      <TouchableOpacity
        key={group.id}
        style={styles.groupItem}
        onPress={() => navigation.navigate('GroupDetails', { groupId: group.id })}
      >
        <View style={styles.groupInfo}>
          <Text style={styles.groupName}>{group.name}</Text>
          {teacher && (
            <Text style={styles.teacherName}>
              Teacher: {teacher.name} {teacher.familyName}
            </Text>
          )}
          <Text style={styles.studentCount}>
            Students: {group.studentIds.length}
          </Text>
        </View>
        <Text style={styles.arrow}>→</Text>
      </TouchableOpacity>
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Groups</Text>
        <TouchableOpacity
          onPress={() => navigation.navigate('AddGroup')}
          style={styles.addButton}
        >
          <Text style={styles.addButtonText}>+</Text>
        </TouchableOpacity>
      </View>

      <ScrollView style={styles.content}>
        {groups.length > 0 ? (
          groups.map(renderGroupItem)
        ) : (
          <View style={styles.emptyState}>
            <Text style={styles.emptyStateText}>No groups found</Text>
            <TouchableOpacity
              onPress={() => navigation.navigate('AddGroup')}
              style={styles.emptyStateButton}
            >
              <Text style={styles.emptyStateButtonText}>Add New Group</Text>
            </TouchableOpacity>
          </View>
        )}
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
  addButton: {
    padding: 10,
  },
  addButtonText: {
    fontSize: 24,
    color: '#007AFF',
  },
  content: {
    flex: 1,
    padding: 20,
  },
  groupItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#fff',
    borderRadius: 15,
    padding: 20,
    marginBottom: 15,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 3,
  },
  groupInfo: {
    flex: 1,
  },
  groupName: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginBottom: 5,
  },
  teacherName: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  studentCount: {
    fontSize: 14,
    color: '#666',
  },
  arrow: {
    fontSize: 20,
    color: '#007AFF',
    marginLeft: 10,
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  emptyStateText: {
    fontSize: 16,
    color: '#666',
    marginBottom: 20,
  },
  emptyStateButton: {
    backgroundColor: '#007AFF',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 10,
  },
  emptyStateButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
}); 
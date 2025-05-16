import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, FlatList } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { useTeachers } from '../context/TeacherContext';
import { NavigationProp } from '../types/navigation';
import { toast } from 'sonner-native';

export default function TeacherListScreen() {
  const navigation = useNavigation<NavigationProp>();
  const { teachers, deleteTeacher } = useTeachers();

  const handleBack = () => {
    navigation.goBack();
  };

  const handleAddTeacher = () => {
    navigation.navigate('AddTeacher');
  };

  const handleTeacherPress = (teacher: any) => {
    navigation.navigate('TeacherDetails', { teacherId: teacher.id });
  };

  const handleDeleteTeacher = async (id: string) => {
    try {
      await deleteTeacher(id);
      toast.success('Teacher deleted successfully');
    } catch (error) {
      toast.error('Failed to delete teacher');
    }
  };

  const renderTeacherItem = ({ item }: { item: any }) => (
    <TouchableOpacity
      style={styles.teacherCard}
      onPress={() => handleTeacherPress(item)}
    >
      <View style={styles.teacherInfo}>
        <Text style={styles.teacherName}>{item.name} {item.familyName}</Text>
        <Text style={styles.teacherSubject}>{item.subject}</Text>
        <Text style={styles.teacherGroup}>{item.group}</Text>
      </View>
      <TouchableOpacity
        style={styles.deleteButton}
        onPress={() => handleDeleteTeacher(item.id)}
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
        <Text style={styles.title}>Teachers</Text>
        <TouchableOpacity style={styles.addButton} onPress={handleAddTeacher}>
          <Text style={styles.addButtonText}>+</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={teachers}
        renderItem={renderTeacherItem}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.listContainer}
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
  },
  addButton: {
    backgroundColor: '#007AFF',
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  addButtonText: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
  },
  listContainer: {
    padding: 20,
  },
  teacherCard: {
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 12,
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
  teacherInfo: {
    flex: 1,
  },
  teacherName: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginBottom: 4,
  },
  teacherSubject: {
    fontSize: 14,
    color: '#666',
    marginBottom: 2,
  },
  teacherGroup: {
    fontSize: 14,
    color: '#666',
  },
  deleteButton: {
    backgroundColor: '#FF3B30',
    padding: 8,
    borderRadius: 6,
    marginLeft: 10,
  },
  deleteButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '600',
  },
}); 
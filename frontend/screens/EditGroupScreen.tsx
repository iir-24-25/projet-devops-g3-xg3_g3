import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, TextInput } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { toast } from 'sonner-native';
import { Picker } from '@react-native-picker/picker';
import { useGroups } from '../context/GroupContext';
import { useTeachers } from '../context/TeacherContext';
import { useStudents } from '../context/StudentContext';
import { NavigationProp } from '../types/navigation';

export default function EditGroupScreen() {
  const navigation = useNavigation<NavigationProp>();
  const route = useRoute<RouteProp<{ EditGroup: { groupId: string } }>>();
  const { groups, updateGroup } = useGroups();
  const { teachers } = useTeachers();
  const { students } = useStudents();

  const group = groups.find(g => g.id === route.params.groupId);
  const [formData, setFormData] = useState({
    name: '',
    teacherId: '',
    studentIds: [] as string[],
  });

  // Transform data for pickers
  const teacherItems = teachers.map(teacher => ({
    label: `${teacher.name} ${teacher.familyName} (${teacher.subject})`,
    value: teacher.id,
  }));

  const studentItems = students
    .filter(student => student.role === 'student')
    .map(student => ({
      label: `${student.name} ${student.familyName}`,
      value: student.id,
    }));

  useEffect(() => {
    if (group) {
      setFormData({
        name: group.name,
        teacherId: group.teacherId,
        studentIds: group.studentIds,
      });
    }
  }, [group]);

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

  const handleUpdateGroup = async () => {
    if (!formData.name || !formData.teacherId || formData.studentIds.length === 0) {
      toast.error('Please fill in all fields');
      return;
    }

    try {
      await updateGroup(group.id, {
        name: formData.name,
        teacherId: formData.teacherId,
        studentIds: formData.studentIds,
      });
      toast.success('Group updated successfully');
      navigation.goBack();
    } catch (error) {
      toast.error('Failed to update group');
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Edit Group</Text>
        <TouchableOpacity onPress={handleUpdateGroup} style={styles.saveButton}>
          <Text style={styles.saveButtonText}>Save</Text>
        </TouchableOpacity>
      </View>

      <ScrollView style={styles.content}>
        <View style={styles.form}>
          <View style={styles.inputGroup}>
            <Text style={styles.label}>Group Name</Text>
            <TextInput
              style={styles.input}
              value={formData.name}
              onChangeText={(text) => setFormData(prev => ({ ...prev, name: text }))}
              placeholder="Enter group name"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Teacher</Text>
            <View style={styles.pickerContainer}>
              <Picker
                selectedValue={formData.teacherId}
                onValueChange={(value) => setFormData(prev => ({ ...prev, teacherId: value }))}
                style={styles.picker}
              >
                <Picker.Item label="Select a teacher" value="" />
                {teacherItems.map((item) => (
                  <Picker.Item key={item.value} label={item.label} value={item.value} />
                ))}
              </Picker>
            </View>
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Students</Text>
            <View style={styles.pickerContainer}>
              <Picker
                selectedValue={formData.studentIds[0] || ''}
                onValueChange={(value) => {
                  if (value && !formData.studentIds.includes(value)) {
                    setFormData(prev => ({
                      ...prev,
                      studentIds: [...prev.studentIds, value]
                    }));
                  }
                }}
                style={styles.picker}
              >
                <Picker.Item label="Select students" value="" />
                {studentItems.map((item) => (
                  <Picker.Item key={item.value} label={item.label} value={item.value} />
                ))}
              </Picker>
            </View>
            {formData.studentIds.length > 0 && (
              <View style={styles.selectedStudents}>
                {formData.studentIds.map((id) => {
                  const student = studentItems.find(item => item.value === id);
                  return (
                    <View key={id} style={styles.selectedStudent}>
                      <Text style={styles.selectedStudentText}>{student?.label}</Text>
                      <TouchableOpacity
                        onPress={() => {
                          setFormData(prev => ({
                            ...prev,
                            studentIds: prev.studentIds.filter(studentId => studentId !== id)
                          }));
                        }}
                      >
                        <Text style={styles.removeStudentText}>×</Text>
                      </TouchableOpacity>
                    </View>
                  );
                })}
              </View>
            )}
          </View>
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
  saveButton: {
    padding: 10,
  },
  saveButtonText: {
    fontSize: 16,
    color: '#007AFF',
    fontWeight: '600',
  },
  content: {
    flex: 1,
    padding: 20,
  },
  form: {
    backgroundColor: '#fff',
    borderRadius: 15,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 3,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 10,
  },
  input: {
    backgroundColor: '#f8f8f8',
    borderRadius: 10,
    padding: 15,
    fontSize: 16,
    color: '#333',
    borderWidth: 1,
    borderColor: '#ddd',
  },
  pickerContainer: {
    backgroundColor: '#f8f8f8',
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#ddd',
    overflow: 'hidden',
  },
  picker: {
    height: 50,
    width: '100%',
  },
  selectedStudents: {
    marginTop: 10,
  },
  selectedStudent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#e3f2fd',
    padding: 10,
    borderRadius: 5,
    marginBottom: 5,
  },
  selectedStudentText: {
    color: '#1976d2',
    fontSize: 14,
  },
  removeStudentText: {
    color: '#f44336',
    fontSize: 18,
    fontWeight: 'bold',
    paddingHorizontal: 5,
  },
}); 
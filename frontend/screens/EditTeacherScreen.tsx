import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, TextInput, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { NavigationProp, Teacher } from '../types/navigation';
import { useTeachers } from '../context/TeacherContext';
import { toast } from 'sonner-native';

type EditTeacherParams = {
  EditTeacher: {
    teacher: Teacher;
  };
};

export default function EditTeacherScreen() {
  const navigation = useNavigation<NavigationProp>();
  const route = useRoute<RouteProp<EditTeacherParams, 'EditTeacher'>>();
  const { teacher } = route.params;
  const { updateTeacher } = useTeachers();

  const [formData, setFormData] = useState({
    name: teacher.name,
    familyName: teacher.familyName,
    subject: teacher.subject,
    group: teacher.group,
    role: 'teacher' as const,
  });

  const handleBack = () => {
    navigation.goBack();
  };

  const handleUpdateTeacher = () => {
    // Validate form data
    if (!formData.name || !formData.familyName || !formData.subject || !formData.group) {
      toast.error('Please fill in all fields');
      return;
    }

    // Update teacher
    const updatedTeacher: Teacher = {
      ...teacher,
      ...formData,
    };

    updateTeacher(teacher.id, formData);
    toast.success('Teacher updated successfully');
    navigation.goBack();
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.content}>
        <View style={styles.header}>
          <TouchableOpacity onPress={handleBack} style={styles.backButton}>
            <Text style={styles.backButtonText}>←</Text>
          </TouchableOpacity>
          <Text style={styles.title}>Edit Teacher</Text>
          <TouchableOpacity style={styles.saveButton} onPress={handleUpdateTeacher}>
            <Text style={styles.saveButtonText}>Save</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.form}>
          <View style={styles.inputGroup}>
            <Text style={styles.label}>First Name</Text>
            <TextInput
              style={styles.input}
              value={formData.name}
              onChangeText={(text) => setFormData({ ...formData, name: text })}
              placeholder="Enter first name"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Family Name</Text>
            <TextInput
              style={styles.input}
              value={formData.familyName}
              onChangeText={(text) => setFormData({ ...formData, familyName: text })}
              placeholder="Enter family name"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Subject</Text>
            <TextInput
              style={styles.input}
              value={formData.subject}
              onChangeText={(text) => setFormData({ ...formData, subject: text })}
              placeholder="Enter subject"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Group</Text>
            <TextInput
              style={styles.input}
              value={formData.group}
              onChangeText={(text) => setFormData({ ...formData, group: text })}
              placeholder="Enter group"
            />
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
  content: {
    flex: 1,
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
  saveButton: {
    backgroundColor: '#007AFF',
    padding: 10,
    borderRadius: 8,
  },
  saveButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '600',
  },
  form: {
    padding: 20,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 8,
  },
  input: {
    backgroundColor: '#fff',
    padding: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#ddd',
    fontSize: 16,
  },
}); 
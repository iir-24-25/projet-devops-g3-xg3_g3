import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, TextInput, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { useTeachers } from '../context/TeacherContext';
import { NavigationProp } from '../types/navigation';
import { toast } from 'sonner-native';

export default function AddTeacherScreen() {
  const navigation = useNavigation<NavigationProp>();
  const { addTeacher } = useTeachers();

  const [formData, setFormData] = useState({
    name: '',
    familyName: '',
    subject: '',
    group: '',
    role: 'teacher' as const,
  });

  const handleBack = () => {
    navigation.goBack();
  };

  const handleAddTeacher = async () => {
    try {
      // Validate form data
      if (!formData.name || !formData.familyName || !formData.subject || !formData.group) {
        toast.error('Please fill in all fields');
        return;
      }

      await addTeacher(formData);
      toast.success('Teacher added successfully');
      navigation.goBack();
    } catch (error) {
      toast.error('Failed to add teacher');
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.content}>
        <View style={styles.header}>
          <TouchableOpacity onPress={handleBack} style={styles.backButton}>
            <Text style={styles.backButtonText}>←</Text>
          </TouchableOpacity>
          <Text style={styles.title}>Add Teacher</Text>
          <TouchableOpacity style={styles.addButton} onPress={handleAddTeacher}>
            <Text style={styles.addButtonText}>Add</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.formContainer}>
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
  formContainer: {
    backgroundColor: '#fff',
    margin: 20,
    padding: 20,
    borderRadius: 12,
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
    fontSize: 14,
    color: '#666',
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    color: '#333',
  },
}); 
import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Platform,
  Alert,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useAssignments } from '../context/AssignmentContext';
import { useGroups } from '../context/GroupContext';
import * as DocumentPicker from 'expo-document-picker';
import { Picker } from '@react-native-picker/picker';
import DateTimePicker from '@react-native-community/datetimepicker';

type AddAssignmentScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'AddAssignment'>;

export default function AddAssignmentScreen() {
  const navigation = useNavigation<AddAssignmentScreenNavigationProp>();
  const { addAssignment } = useAssignments();
  const { groups } = useGroups();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [groupId, setGroupId] = useState('');
  const [dueDate, setDueDate] = useState(new Date().toISOString().split('T')[0]);
  const [file, setFile] = useState<{ uri: string; name: string } | null>(null);
  const [showDatePicker, setShowDatePicker] = useState(false);

  const handleFilePick = async () => {
    try {
      const result = await DocumentPicker.getDocumentAsync({
        type: '*/*',
        copyToCacheDirectory: true,
      });

      if (!result.canceled) {
        setFile({
          uri: result.assets[0].uri,
          name: result.assets[0].name,
        });
      }
    } catch (error) {
      console.error('Error picking file:', error);
      Alert.alert('Error', 'Failed to pick file');
    }
  };

  const handleSubmit = () => {
    if (!title || !groupId) {
      Alert.alert('Error', 'Please fill in all required fields');
      return;
    }

    addAssignment({
      title,
      description,
      groupId,
      dueDate: new Date(dueDate).toISOString(),
      fileUrl: file?.uri || '',
      fileName: file?.name || '',
      createdBy: 'admin', // TODO: Get from auth context
    });

    navigation.goBack();
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.form}>
        <Text style={styles.label}>Title *</Text>
        <TextInput
          style={styles.input}
          value={title}
          onChangeText={setTitle}
          placeholder="Enter assignment title"
        />

        <Text style={styles.label}>Description</Text>
        <TextInput
          style={[styles.input, styles.textArea]}
          value={description}
          onChangeText={setDescription}
          placeholder="Enter assignment description"
          multiline
          numberOfLines={4}
        />

        <Text style={styles.label}>Group *</Text>
        <View style={styles.pickerContainer}>
          <Picker
            selectedValue={groupId}
            onValueChange={(value) => setGroupId(value)}
            style={styles.picker}
          >
            <Picker.Item label="Select a group" value="" />
            {groups.map((group) => (
              <Picker.Item key={group.id} label={group.name} value={group.id} />
            ))}
          </Picker>
        </View>

        <Text style={styles.label}>Due Date *</Text>
        {Platform.OS === 'web' ? (
          <input
            type="date"
            value={dueDate}
            onChange={e => setDueDate(e.target.value)}
            style={{
              padding: 12,
              borderRadius: 8,
              border: '1px solid #ddd',
              marginBottom: 16,
              fontSize: 16,
              width: '100%',
            }}
          />
        ) : (
          <>
            <TouchableOpacity
              style={styles.input}
              onPress={() => setShowDatePicker(true)}
            >
              <Text style={styles.dateText}>{dueDate}</Text>
            </TouchableOpacity>
            {showDatePicker && (
              <DateTimePicker
                value={new Date(dueDate)}
                mode="date"
                display="default"
                onChange={(event, selectedDate) => {
                  setShowDatePicker(false);
                  if (selectedDate) {
                    setDueDate(selectedDate.toISOString().split('T')[0]);
                  }
                }}
              />
            )}
          </>
        )}

        <TouchableOpacity style={styles.fileButton} onPress={handleFilePick}>
          <Text style={styles.fileButtonText}>
            {file ? `Selected: ${file.name}` : 'Select File'}
          </Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
          <Text style={styles.submitButtonText}>Create Assignment</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  form: {
    padding: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 8,
  },
  input: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    marginBottom: 16,
    fontSize: 16,
  },
  textArea: {
    height: 100,
    textAlignVertical: 'top',
  },
  pickerContainer: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    marginBottom: 16,
  },
  picker: {
    height: 50,
  },
  fileButton: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    marginBottom: 16,
    alignItems: 'center',
  },
  fileButtonText: {
    color: '#007AFF',
    fontSize: 16,
  },
  submitButton: {
    backgroundColor: '#007AFF',
    borderRadius: 8,
    padding: 16,
    alignItems: 'center',
  },
  submitButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
  dateText: {
    fontSize: 16,
    color: '#333',
  },
}); 
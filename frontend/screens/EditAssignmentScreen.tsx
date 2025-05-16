import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  ScrollView,
  Alert,
  Platform,
} from 'react-native';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useAssignments } from '../context/AssignmentContext';
import { useGroups } from '../context/GroupContext';
import { useAuth } from '../context/AuthContext';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as DocumentPicker from 'expo-document-picker';

type EditAssignmentScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'EditAssignment'>;
type EditAssignmentScreenRouteProp = RouteProp<RootStackParamList, 'EditAssignment'>;

const EditAssignmentScreen = () => {
  const navigation = useNavigation<EditAssignmentScreenNavigationProp>();
  const route = useRoute<EditAssignmentScreenRouteProp>();
  const { assignmentId } = route.params;
  const { getAssignmentById, updateAssignment } = useAssignments();
  const { groups = [] } = useGroups();
  const { user } = useAuth();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [groupId, setGroupId] = useState('');
  const [dueDate, setDueDate] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [file, setFile] = useState<{ uri: string; name: string } | null>(null);

  useEffect(() => {
    const assignment = getAssignmentById(assignmentId);
    if (assignment) {
      setTitle(assignment.title);
      setDescription(assignment.description);
      setGroupId(assignment.groupId);
      setDueDate(new Date(assignment.dueDate));
      if (assignment.fileUrl) {
        setFile({ uri: assignment.fileUrl, name: assignment.fileName });
      }
    }
  }, [assignmentId]);

  const handlePickDocument = async () => {
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
      console.error('Error picking document:', error);
      Alert.alert('Error', 'Failed to pick document');
    }
  };

  const handleDateChange = (event: any, selectedDate?: Date) => {
    setShowDatePicker(false);
    if (selectedDate) {
      setDueDate(selectedDate);
    }
  };

  const handleSubmit = async () => {
    if (!title.trim()) {
      Alert.alert('Error', 'Please enter a title');
      return;
    }

    if (!description.trim()) {
      Alert.alert('Error', 'Please enter a description');
      return;
    }

    if (!groupId) {
      Alert.alert('Error', 'Please select a group');
      return;
    }

    if (!user) {
      Alert.alert('Error', 'You must be logged in to update an assignment');
      return;
    }

    try {
      const updatedAssignment = {
        title: title.trim(),
        description: description.trim(),
        groupId,
        dueDate: dueDate.toISOString(),
        fileUrl: file?.uri || '',
        fileName: file?.name || '',
        status: 'active' as const,
        createdBy: user.id,
      };

      updateAssignment(assignmentId, updatedAssignment);
      Alert.alert('Success', 'Assignment updated successfully');
      navigation.goBack();
    } catch (error) {
      console.error('Error updating assignment:', error);
      Alert.alert('Error', 'Failed to update assignment');
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Edit Assignment</Text>
      </View>

      <ScrollView style={styles.content}>
        <View style={styles.formGroup}>
          <Text style={styles.label}>Title</Text>
          <TextInput
            style={styles.input}
            value={title}
            onChangeText={setTitle}
            placeholder="Enter assignment title"
            placeholderTextColor="#999"
          />
        </View>

        <View style={styles.formGroup}>
          <Text style={styles.label}>Description</Text>
          <TextInput
            style={[styles.input, styles.textArea]}
            value={description}
            onChangeText={setDescription}
            placeholder="Enter assignment description"
            placeholderTextColor="#999"
            multiline
            numberOfLines={4}
          />
        </View>

        <View style={styles.formGroup}>
          <Text style={styles.label}>Group</Text>
          <View style={styles.groupSelector}>
            {groups.map((group) => (
              <TouchableOpacity
                key={group.id}
                style={[
                  styles.groupOption,
                  groupId === group.id && styles.selectedGroup,
                ]}
                onPress={() => setGroupId(group.id)}
              >
                <Text
                  style={[
                    styles.groupOptionText,
                    groupId === group.id && styles.selectedGroupText,
                  ]}
                >
                  {group.name}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        <View style={styles.formGroup}>
          <Text style={styles.label}>Due Date</Text>
          <TouchableOpacity
            style={styles.dateButton}
            onPress={() => setShowDatePicker(true)}
          >
            <Text style={styles.dateButtonText}>
              {dueDate.toLocaleDateString()}
            </Text>
          </TouchableOpacity>
          {showDatePicker && (
            <DateTimePicker
              value={dueDate}
              mode="date"
              display={Platform.OS === 'ios' ? 'spinner' : 'default'}
              onChange={handleDateChange}
              minimumDate={new Date()}
            />
          )}
        </View>

        <View style={styles.formGroup}>
          <Text style={styles.label}>Assignment File</Text>
          <TouchableOpacity
            style={styles.fileButton}
            onPress={handlePickDocument}
          >
            <Text style={styles.fileButtonText}>
              {file ? file.name : 'Select File'}
            </Text>
          </TouchableOpacity>
        </View>

        <TouchableOpacity
          style={styles.submitButton}
          onPress={handleSubmit}
        >
          <Text style={styles.submitButtonText}>Update Assignment</Text>
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  backButton: {
    marginRight: 16,
  },
  backButtonText: {
    fontSize: 24,
    color: '#1a73e8',
  },
  headerTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#202124',
  },
  content: {
    flex: 1,
    padding: 16,
  },
  formGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '500',
    color: '#202124',
    marginBottom: 8,
  },
  input: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    color: '#202124',
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  textArea: {
    height: 100,
    textAlignVertical: 'top',
  },
  groupSelector: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  groupOption: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  selectedGroup: {
    backgroundColor: '#1a73e8',
    borderColor: '#1a73e8',
  },
  groupOptionText: {
    fontSize: 14,
    color: '#202124',
  },
  selectedGroupText: {
    color: '#fff',
  },
  dateButton: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 12,
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  dateButtonText: {
    fontSize: 16,
    color: '#202124',
  },
  fileButton: {
    backgroundColor: '#1a73e8',
    borderRadius: 8,
    padding: 12,
    alignItems: 'center',
  },
  fileButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
  submitButton: {
    backgroundColor: '#0f9d58',
    borderRadius: 8,
    padding: 16,
    alignItems: 'center',
    marginTop: 24,
  },
  submitButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
});

export default EditAssignmentScreen; 
import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  TextInput,
  Alert,
  Linking,
} from 'react-native';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useSubmissions } from '../context/SubmissionContext';
import { useStudents } from '../context/StudentContext';
import { useAssignments } from '../context/AssignmentContext';
import { useRole } from '../context/RoleContext';

type SubmissionDetailsScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'SubmissionDetails'>;
type SubmissionDetailsScreenRouteProp = RouteProp<RootStackParamList, 'SubmissionDetails'>;

export default function SubmissionDetailsScreen() {
  const navigation = useNavigation<SubmissionDetailsScreenNavigationProp>();
  const route = useRoute<SubmissionDetailsScreenRouteProp>();
  const { submissionId } = route.params;
  const { getSubmissionById, updateSubmission } = useSubmissions();
  const { students } = useStudents();
  const { getAssignmentById } = useAssignments();
  const { isTeacher } = useRole();

  const submission = getSubmissionById(submissionId);
  const [feedback, setFeedback] = useState(submission?.feedback || '');
  const [grade, setGrade] = useState(submission?.grade?.toString() || '');

  const handleBack = () => {
    navigation.goBack();
  };

  const handleOpenFile = async (fileUrl: string) => {
    try {
      const supported = await Linking.canOpenURL(fileUrl);
      if (supported) {
        await Linking.openURL(fileUrl);
      }
    } catch (error) {
      console.error('Error opening file:', error);
    }
  };

  const handleSaveReview = () => {
    if (!submission) return;

    const gradeNumber = parseFloat(grade);
    if (isNaN(gradeNumber) || gradeNumber < 0 || gradeNumber > 100) {
      Alert.alert('Invalid Grade', 'Please enter a valid grade between 0 and 100');
      return;
    }

    updateSubmission(submission.id, {
      ...submission,
      status: 'reviewed',
      feedback,
      grade: gradeNumber,
    });

    Alert.alert('Success', 'Review saved successfully', [
      { text: 'OK', onPress: handleBack }
    ]);
  };

  const getStudentName = (studentId: string) => {
    const student = students.find(s => s.id === studentId);
    return student ? `${student.name} ${student.familyName}` : 'Unknown Student';
  };

  if (!submission) {
    return (
      <View style={styles.container}>
        <Text style={styles.errorText}>Submission not found</Text>
      </View>
    );
  }

  const assignment = getAssignmentById(submission.assignmentId);

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={handleBack}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Review Submission</Text>
      </View>

      <View style={styles.content}>
        <View style={styles.infoCard}>
          <Text style={styles.label}>Student</Text>
          <Text style={styles.value}>{getStudentName(submission.studentId)}</Text>

          <Text style={styles.label}>Assignment</Text>
          <Text style={styles.value}>{assignment?.title || 'Unknown Assignment'}</Text>

          <Text style={styles.label}>Submitted</Text>
          <Text style={styles.value}>
            {new Date(submission.submittedAt).toLocaleString()}
          </Text>

          <Text style={styles.label}>Status</Text>
          <Text style={[
            styles.value,
            styles.status,
            submission.status === 'reviewed' ? styles.reviewedStatus : styles.pendingStatus
          ]}>
            {submission.status}
          </Text>
        </View>

        <View style={styles.fileCard}>
          <Text style={styles.label}>Submission File</Text>
          <TouchableOpacity
            style={styles.fileButton}
            onPress={() => handleOpenFile(submission.fileUrl)}
          >
            <Text style={styles.fileButtonText}>{submission.fileName}</Text>
          </TouchableOpacity>
        </View>

        {isTeacher && (
          <View style={styles.reviewCard}>
            <Text style={styles.label}>Grade (0-100)</Text>
            <TextInput
              style={styles.gradeInput}
              value={grade}
              onChangeText={setGrade}
              keyboardType="numeric"
              placeholder="Enter grade"
            />

            <Text style={styles.label}>Feedback</Text>
            <TextInput
              style={styles.feedbackInput}
              value={feedback}
              onChangeText={setFeedback}
              placeholder="Enter feedback"
              multiline
              numberOfLines={4}
            />

            <TouchableOpacity
              style={styles.saveButton}
              onPress={handleSaveReview}
            >
              <Text style={styles.saveButtonText}>Save Review</Text>
            </TouchableOpacity>
          </View>
        )}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
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
  title: {
    fontSize: 20,
    fontWeight: '600',
    color: '#202124',
  },
  content: {
    padding: 16,
  },
  infoCard: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  fileCard: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  reviewCard: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  label: {
    fontSize: 14,
    color: '#5f6368',
    marginBottom: 4,
  },
  value: {
    fontSize: 16,
    color: '#202124',
    marginBottom: 16,
  },
  status: {
    fontSize: 14,
    fontWeight: '500',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
    alignSelf: 'flex-start',
  },
  pendingStatus: {
    backgroundColor: '#fef3c7',
    color: '#92400e',
  },
  reviewedStatus: {
    backgroundColor: '#dcfce7',
    color: '#166534',
  },
  fileButton: {
    backgroundColor: '#f8f9fa',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
  },
  fileButtonText: {
    color: '#1a73e8',
    fontSize: 14,
    fontWeight: '500',
  },
  gradeInput: {
    backgroundColor: '#f8f9fa',
    padding: 12,
    borderRadius: 8,
    marginBottom: 16,
    fontSize: 16,
  },
  feedbackInput: {
    backgroundColor: '#f8f9fa',
    padding: 12,
    borderRadius: 8,
    marginBottom: 16,
    fontSize: 16,
    textAlignVertical: 'top',
    minHeight: 100,
  },
  saveButton: {
    backgroundColor: '#1a73e8',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
  },
  saveButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
  errorText: {
    fontSize: 16,
    color: '#dc3545',
    textAlign: 'center',
    marginTop: 20,
  },
}); 
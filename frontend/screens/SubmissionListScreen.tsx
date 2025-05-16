import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Linking,
} from 'react-native';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useSubmissions } from '../context/SubmissionContext';
import { useStudents } from '../context/StudentContext';
import { useAssignments } from '../context/AssignmentContext';
import { useRole } from '../context/RoleContext';

type SubmissionListScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'SubmissionList'>;
type SubmissionListScreenRouteProp = RouteProp<RootStackParamList, 'SubmissionList'>;

export default function SubmissionListScreen() {
  const navigation = useNavigation<SubmissionListScreenNavigationProp>();
  const route = useRoute<SubmissionListScreenRouteProp>();
  const { assignmentId } = route.params;
  const { getSubmissionsByAssignment } = useSubmissions();
  const { students } = useStudents();
  const { getAssignmentById } = useAssignments();
  const { isTeacher } = useRole();

  const assignment = getAssignmentById(assignmentId);
  const submissions = getSubmissionsByAssignment(assignmentId);

  const handleBack = () => {
    navigation.goBack();
  };

  const handleViewSubmission = (submissionId: string) => {
    navigation.navigate('SubmissionDetails', { submissionId });
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

  const getStudentName = (studentId: string) => {
    const student = students.find(s => s.id === studentId);
    return student ? `${student.name} ${student.familyName}` : 'Unknown Student';
  };

  if (!assignment) {
    return (
      <View style={styles.container}>
        <Text style={styles.errorText}>Assignment not found</Text>
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={handleBack}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Submissions for {assignment.title}</Text>
      </View>

      <View style={styles.content}>
        {submissions.length === 0 ? (
          <Text style={styles.emptyText}>No submissions yet</Text>
        ) : (
          submissions.map(submission => (
            <View key={submission.id} style={styles.submissionCard}>
              <View style={styles.submissionHeader}>
                <Text style={styles.studentName}>
                  {getStudentName(submission.studentId)}
                </Text>
                <Text style={[
                  styles.status,
                  submission.status === 'reviewed' ? styles.reviewedStatus : styles.pendingStatus
                ]}>
                  {submission.status}
                </Text>
              </View>

              <View style={styles.submissionDetails}>
                <Text style={styles.submissionDate}>
                  Submitted: {new Date(submission.submittedAt).toLocaleDateString()}
                </Text>
                {submission.grade && (
                  <Text style={styles.grade}>
                    Grade: {submission.grade}
                  </Text>
                )}
              </View>

              <View style={styles.buttonContainer}>
                <TouchableOpacity
                  style={styles.fileButton}
                  onPress={() => handleOpenFile(submission.fileUrl)}
                >
                  <Text style={styles.fileButtonText}>View Submission</Text>
                </TouchableOpacity>

                {isTeacher && (
                  <TouchableOpacity
                    style={styles.reviewButton}
                    onPress={() => handleViewSubmission(submission.id)}
                  >
                    <Text style={styles.reviewButtonText}>
                      {submission.status === 'reviewed' ? 'Update Review' : 'Review'}
                    </Text>
                  </TouchableOpacity>
                )}
              </View>
            </View>
          ))
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
  emptyText: {
    textAlign: 'center',
    fontSize: 16,
    color: '#5f6368',
    marginTop: 32,
  },
  submissionCard: {
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
  submissionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  studentName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#202124',
  },
  status: {
    fontSize: 14,
    fontWeight: '500',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
  },
  pendingStatus: {
    backgroundColor: '#fef3c7',
    color: '#92400e',
  },
  reviewedStatus: {
    backgroundColor: '#dcfce7',
    color: '#166534',
  },
  submissionDetails: {
    marginBottom: 12,
  },
  submissionDate: {
    fontSize: 14,
    color: '#5f6368',
    marginBottom: 4,
  },
  grade: {
    fontSize: 14,
    color: '#1a73e8',
    fontWeight: '500',
  },
  buttonContainer: {
    flexDirection: 'row',
    gap: 12,
  },
  fileButton: {
    flex: 1,
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
  reviewButton: {
    flex: 1,
    backgroundColor: '#1a73e8',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
  },
  reviewButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '500',
  },
  errorText: {
    fontSize: 16,
    color: '#dc3545',
    textAlign: 'center',
    marginTop: 20,
  },
}); 
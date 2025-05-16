import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  TextInput,
  ActivityIndicator,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation, useRoute } from '@react-navigation/native';
import { NavigationProp } from '../types/navigation';
import { toast } from 'sonner-native';
import { useAssignments } from '../context/AssignmentContext';
import { useStudents } from '../context/StudentContext';
import { useGrades, Grade } from '../context/GradeContext';

export default function GradeAssignmentScreen() {
  const navigation = useNavigation<NavigationProp>();
  const route = useRoute();
  const { assignmentId } = route.params as { assignmentId: string };
  const { assignments } = useAssignments();
  const { students } = useStudents();
  const { grades, addGrade, updateGrade, getGrade } = useGrades();
  const [isLoading, setIsLoading] = useState(false);
  const [studentGrades, setStudentGrades] = useState<{
    [key: string]: { grade: string; feedback: string };
  }>({});

  const assignment = assignments.find(a => a.id === assignmentId);
  const groupStudents = students.filter(student => student.groupId === assignment?.groupId);

  useEffect(() => {
    // Initialize grades for students
    const initialGrades: { [key: string]: { grade: string; feedback: string } } = {};
    groupStudents.forEach(student => {
      const existingGrade = getGrade(assignmentId, student.id);
      initialGrades[student.id] = {
        grade: existingGrade?.grade.toString() || '',
        feedback: existingGrade?.feedback || '',
      };
    });
    setStudentGrades(initialGrades);
  }, [assignmentId, groupStudents]);

  const handleBack = () => {
    navigation.goBack();
  };

  const handleGradeChange = (studentId: string, value: string) => {
    setStudentGrades(prev => ({
      ...prev,
      [studentId]: { ...prev[studentId], grade: value },
    }));
  };

  const handleFeedbackChange = (studentId: string, value: string) => {
    setStudentGrades(prev => ({
      ...prev,
      [studentId]: { ...prev[studentId], feedback: value },
    }));
  };

  const handleSaveGrades = async () => {
    setIsLoading(true);
    try {
      for (const student of groupStudents) {
        const gradeData = studentGrades[student.id];
        if (!gradeData.grade) continue;

        const gradeValue = parseFloat(gradeData.grade);
        if (isNaN(gradeValue) || gradeValue < 0 || gradeValue > 100) {
          toast.error(`Invalid grade for ${student.name}`);
          continue;
        }

        const existingGrade = getGrade(assignmentId, student.id);
        if (existingGrade) {
          await updateGrade(existingGrade.id, {
            grade: gradeValue,
            feedback: gradeData.feedback,
          });
        } else {
          await addGrade({
            assignmentId,
            studentId: student.id,
            grade: gradeValue,
            feedback: gradeData.feedback,
            submittedAt: new Date().toISOString(),
          });
        }
      }
      toast.success('Grades saved successfully');
      navigation.goBack();
    } catch (error) {
      console.error('Error saving grades:', error);
      toast.error('Failed to save grades');
    } finally {
      setIsLoading(false);
    }
  };

  if (!assignment) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>Assignment not found</Text>
          <TouchableOpacity style={styles.button} onPress={handleBack}>
            <Text style={styles.buttonText}>Go Back</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={handleBack} style={styles.backButton}>
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Grade Assignment</Text>
        <View style={styles.placeholder} />
      </View>

      <ScrollView style={styles.content}>
        <View style={styles.assignmentInfo}>
          <Text style={styles.assignmentTitle}>{assignment.title}</Text>
          <Text style={styles.assignmentDescription}>{assignment.description}</Text>
        </View>

        <View style={styles.gradesContainer}>
          {groupStudents.map(student => (
            <View key={student.id} style={styles.studentGradeCard}>
              <Text style={styles.studentName}>
                {student.name} {student.familyName}
              </Text>
              <View style={styles.gradeInputContainer}>
                <TextInput
                  style={styles.gradeInput}
                  placeholder="Grade (0-100)"
                  keyboardType="numeric"
                  value={studentGrades[student.id]?.grade || ''}
                  onChangeText={value => handleGradeChange(student.id, value)}
                  editable={!isLoading}
                />
                <TextInput
                  style={styles.feedbackInput}
                  placeholder="Feedback"
                  multiline
                  value={studentGrades[student.id]?.feedback || ''}
                  onChangeText={value => handleFeedbackChange(student.id, value)}
                  editable={!isLoading}
                />
              </View>
            </View>
          ))}
        </View>

        <TouchableOpacity
          style={[styles.saveButton, isLoading && styles.buttonDisabled]}
          onPress={handleSaveGrades}
          disabled={isLoading}
        >
          {isLoading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.saveButtonText}>Save Grades</Text>
          )}
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#ffffff',
    borderBottomWidth: 1,
    borderBottomColor: '#e8eaed',
  },
  backButton: {
    padding: 8,
  },
  backButtonText: {
    fontSize: 24,
    color: '#1a73e8',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#202124',
  },
  placeholder: {
    width: 40,
  },
  content: {
    flex: 1,
    padding: 16,
  },
  assignmentInfo: {
    backgroundColor: '#ffffff',
    padding: 16,
    borderRadius: 12,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  assignmentTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#202124',
    marginBottom: 8,
  },
  assignmentDescription: {
    fontSize: 14,
    color: '#5f6368',
  },
  gradesContainer: {
    gap: 12,
  },
  studentGradeCard: {
    backgroundColor: '#ffffff',
    padding: 16,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  studentName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#202124',
    marginBottom: 12,
  },
  gradeInputContainer: {
    gap: 8,
  },
  gradeInput: {
    backgroundColor: '#f8f9fa',
    padding: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#e8eaed',
    fontSize: 16,
  },
  feedbackInput: {
    backgroundColor: '#f8f9fa',
    padding: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#e8eaed',
    fontSize: 16,
    minHeight: 80,
    textAlignVertical: 'top',
  },
  saveButton: {
    backgroundColor: '#1a73e8',
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
    marginTop: 24,
    marginBottom: 32,
  },
  buttonDisabled: {
    opacity: 0.7,
  },
  saveButtonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: '600',
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  errorText: {
    fontSize: 16,
    color: '#5f6368',
    marginBottom: 16,
  },
  button: {
    backgroundColor: '#1a73e8',
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 8,
  },
  buttonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: '600',
  },
}); 
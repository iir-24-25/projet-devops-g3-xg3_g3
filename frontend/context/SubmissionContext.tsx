import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Submission } from '../types/navigation';

type SubmissionContextType = {
  submissions: Submission[];
  addSubmission: (submission: Omit<Submission, 'id' | 'submittedAt' | 'status'>) => Promise<void>;
  updateSubmission: (id: string, submission: Partial<Submission>) => Promise<void>;
  deleteSubmission: (id: string) => Promise<void>;
  getSubmissionById: (id: string) => Submission | undefined;
  getSubmissionsByAssignment: (assignmentId: string) => Submission[];
  getSubmissionsByStudent: (studentId: string) => Submission[];
  getSubmissionByAssignmentAndStudent: (assignmentId: string, studentId: string) => Submission | undefined;
};

const SubmissionContext = createContext<SubmissionContextType | undefined>(undefined);

const STORAGE_KEY = '@submissions';

export function SubmissionProvider({ children }: { children: React.ReactNode }) {
  const [submissions, setSubmissions] = useState<Submission[]>([]);

  useEffect(() => {
    loadSubmissions();
  }, []);

  const loadSubmissions = async () => {
    try {
      const storedSubmissions = await AsyncStorage.getItem(STORAGE_KEY);
      if (storedSubmissions) {
        setSubmissions(JSON.parse(storedSubmissions));
      }
    } catch (error) {
      console.error('Error loading submissions:', error);
    }
  };

  const saveSubmissions = async (newSubmissions: Submission[]) => {
    try {
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(newSubmissions));
      setSubmissions(newSubmissions);
    } catch (error) {
      console.error('Error saving submissions:', error);
    }
  };

  const addSubmission = async (submission: Omit<Submission, 'id' | 'submittedAt' | 'status'>) => {
    const newSubmission: Submission = {
      ...submission,
      id: Date.now().toString(),
      submittedAt: new Date().toISOString(),
      status: 'pending',
    };
    await saveSubmissions([...submissions, newSubmission]);
  };

  const updateSubmission = async (id: string, updatedSubmission: Partial<Submission>) => {
    const newSubmissions = submissions.map(submission =>
      submission.id === id
        ? { ...submission, ...updatedSubmission }
        : submission
    );
    await saveSubmissions(newSubmissions);
  };

  const deleteSubmission = async (id: string) => {
    const newSubmissions = submissions.filter(submission => submission.id !== id);
    await saveSubmissions(newSubmissions);
  };

  const getSubmissionById = (id: string) => {
    return submissions.find(submission => submission.id === id);
  };

  const getSubmissionsByAssignment = (assignmentId: string) => {
    return submissions.filter(submission => submission.assignmentId === assignmentId);
  };

  const getSubmissionsByStudent = (studentId: string) => {
    return submissions.filter(submission => submission.studentId === studentId);
  };

  const getSubmissionByAssignmentAndStudent = (assignmentId: string, studentId: string) => {
    return submissions.find(
      submission => submission.assignmentId === assignmentId && submission.studentId === studentId
    );
  };

  return (
    <SubmissionContext.Provider
      value={{
        submissions,
        addSubmission,
        updateSubmission,
        deleteSubmission,
        getSubmissionById,
        getSubmissionsByAssignment,
        getSubmissionsByStudent,
        getSubmissionByAssignmentAndStudent,
      }}
    >
      {children}
    </SubmissionContext.Provider>
  );
}

export function useSubmissions() {
  const context = useContext(SubmissionContext);
  if (context === undefined) {
    throw new Error('useSubmissions must be used within a SubmissionProvider');
  }
  return context;
} 
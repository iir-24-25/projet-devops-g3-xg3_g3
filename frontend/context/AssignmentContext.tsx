import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Assignment } from '../types/navigation';

type AssignmentContextType = {
  assignments: Assignment[];
  addAssignment: (assignment: Omit<Assignment, 'id' | 'createdAt'>) => void;
  updateAssignment: (id: string, assignment: Omit<Assignment, 'id' | 'createdAt'>) => void;
  deleteAssignment: (id: string) => void;
  getAssignmentById: (id: string) => Assignment | undefined;
  getAssignmentsByGroup: (groupId: string) => Assignment[];
};

const AssignmentContext = createContext<AssignmentContextType | undefined>(undefined);

const STORAGE_KEY = '@assignments';

export function AssignmentProvider({ children }: { children: React.ReactNode }) {
  const [assignments, setAssignments] = useState<Assignment[]>([]);

  useEffect(() => {
    loadAssignments();
  }, []);

  const loadAssignments = async () => {
    try {
      const storedAssignments = await AsyncStorage.getItem(STORAGE_KEY);
      if (storedAssignments) {
        setAssignments(JSON.parse(storedAssignments));
      } else {
        // Initialize with default data if no stored data exists
        const defaultAssignments: Assignment[] = [
          {
            id: '1',
            title: 'Mathematics Assignment 1',
            description: 'Complete exercises 1-10 from Chapter 3',
            groupId: '1',
            dueDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString(), // 7 days from now
            fileUrl: '',
            fileName: 'math_assignment1.pdf',
            createdAt: new Date().toISOString(),
            createdBy: '1', // teacherId
            status: 'active'
          },
          {
            id: '2',
            title: 'Physics Lab Report',
            description: 'Write a lab report for the pendulum experiment',
            groupId: '2',
            dueDate: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toISOString(), // 5 days from now
            fileUrl: '',
            fileName: 'physics_lab_report.pdf',
            createdAt: new Date().toISOString(),
            createdBy: '2', // teacherId
            status: 'active'
          },
          {
            id: '3',
            title: 'English Essay',
            description: 'Write a 1000-word essay on Shakespeare\'s Hamlet',
            groupId: '3',
            dueDate: new Date(Date.now() + 10 * 24 * 60 * 60 * 1000).toISOString(), // 10 days from now
            fileUrl: '',
            fileName: 'english_essay.pdf',
            createdAt: new Date().toISOString(),
            createdBy: '3', // teacherId
            status: 'active'
          }
        ];
        setAssignments(defaultAssignments);
        await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(defaultAssignments));
      }
    } catch (error) {
      console.error('Error loading assignments:', error);
    }
  };

  const saveAssignments = async (newAssignments: Assignment[]) => {
    try {
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(newAssignments));
    } catch (error) {
      console.error('Error saving assignments:', error);
    }
  };

  const addAssignment = (assignment: Omit<Assignment, 'id' | 'createdAt'>) => {
    const newAssignment: Assignment = {
      ...assignment,
      id: Date.now().toString(),
      createdAt: new Date().toISOString(),
    };
    const newAssignments = [...assignments, newAssignment];
    setAssignments(newAssignments);
    saveAssignments(newAssignments);
  };

  const updateAssignment = async (id: string, assignment: Omit<Assignment, 'id' | 'createdAt'>) => {
    try {
      const newAssignments = assignments.map(a => 
        a.id === id ? { ...assignment, id, createdAt: a.createdAt } : a
      );
      setAssignments(newAssignments);
      await saveAssignments(newAssignments);
      console.log('Assignment updated successfully:', id);
    } catch (error) {
      console.error('Error updating assignment:', error);
      throw error;
    }
  };

  const deleteAssignment = async (id: string) => {
    console.log('Starting delete operation for assignment:', id);
    console.log('Current assignments before deletion:', assignments);
    
    try {
      // Check if the assignment exists
      const assignmentToDelete = assignments.find(a => a.id === id);
      if (!assignmentToDelete) {
        console.error('Assignment not found:', id);
        return;
      }

      console.log('Found assignment to delete:', assignmentToDelete);

      const newAssignments = assignments.filter(a => a.id !== id);
      console.log('New assignments array after filter:', newAssignments);
      
      // Update state
      setAssignments(newAssignments);
      console.log('State updated with new assignments');
      
      // Save to AsyncStorage
      await saveAssignments(newAssignments);
      console.log('Assignments saved to AsyncStorage');
      
      console.log('Delete operation completed successfully');
    } catch (error) {
      console.error('Error during delete operation:', error);
    }
  };

  const getAssignmentById = (id: string) => {
    return assignments.find(a => a.id === id);
  };

  const getAssignmentsByGroup = (groupId: string) => {
    return assignments.filter(a => a.groupId === groupId);
  };

  return (
    <AssignmentContext.Provider value={{
      assignments,
      addAssignment,
      updateAssignment,
      deleteAssignment,
      getAssignmentById,
      getAssignmentsByGroup,
    }}>
      {children}
    </AssignmentContext.Provider>
  );
}

export function useAssignments() {
  const context = useContext(AssignmentContext);
  if (context === undefined) {
    throw new Error('useAssignments must be used within an AssignmentProvider');
  }
  return context;
} 
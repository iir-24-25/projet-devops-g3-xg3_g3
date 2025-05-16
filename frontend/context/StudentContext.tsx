import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Student } from '../types/navigation';

const STORAGE_KEY = '@students_data';

type StudentContextType = {
  students: Student[];
  addStudent: (student: Omit<Student, 'id'>) => Promise<void>;
  updateStudent: (id: string, studentData: Omit<Student, 'id'>) => Promise<void>;
  deleteStudent: (id: string) => Promise<void>;
};

const StudentContext = createContext<StudentContextType | undefined>(undefined);

export function StudentProvider({ children }: { children: React.ReactNode }) {
  const [students, setStudents] = useState<Student[]>([]);

  // Load students from storage when the app starts
  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = async () => {
    try {
      const storedStudents = await AsyncStorage.getItem(STORAGE_KEY);
      if (storedStudents) {
        setStudents(JSON.parse(storedStudents));
      } else {
        // Initialize with default data if no stored data exists
        const defaultStudents: Student[] = [
          { 
            id: '1', 
            name: 'John', 
            familyName: 'Doe',
            email: 'john.doe@example.com', 
            group: 'Group A',
            role: 'student'
          },
          { 
            id: '2', 
            name: 'Jane', 
            familyName: 'Smith',
            email: 'jane.smith@example.com', 
            group: 'Group B',
            role: 'teacher'
          },
          { 
            id: '3', 
            name: 'Mike', 
            familyName: 'Johnson',
            email: 'mike.johnson@example.com', 
            group: 'Group C',
            role: 'admin'
          },
        ];
        setStudents(defaultStudents);
        await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(defaultStudents));
      }
    } catch (error) {
      console.error('Error loading students:', error);
    }
  };

  const saveStudents = async (updatedStudents: Student[]) => {
    try {
      // First update the state
      setStudents(updatedStudents);
      // Then save to AsyncStorage
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(updatedStudents));
    } catch (error) {
      console.error('Error saving students:', error);
      // If saving fails, revert the state
      setStudents(students);
      throw error;
    }
  };

  const addStudent = async (studentData: Omit<Student, 'id'>) => {
    try {
      const newStudent: Student = {
        id: Date.now().toString(),
        ...studentData,
      };
      await saveStudents([...students, newStudent]);
    } catch (error) {
      console.error('Error adding student:', error);
      throw error;
    }
  };

  const updateStudent = async (id: string, studentData: Omit<Student, 'id'>) => {
    try {
      const updatedStudents = students.map((student) =>
        student.id === id ? { ...student, ...studentData } : student
      );
      await saveStudents(updatedStudents);
    } catch (error) {
      console.error('Error updating student:', error);
      throw error;
    }
  };

  const deleteStudent = async (id: string) => {
    try {
      const updatedStudents = students.filter((student) => student.id !== id);
      await saveStudents(updatedStudents);
    } catch (error) {
      console.error('Error deleting student:', error);
      throw error;
    }
  };

  return (
    <StudentContext.Provider value={{ students, addStudent, updateStudent, deleteStudent }}>
      {children}
    </StudentContext.Provider>
  );
}

export function useStudents() {
  const context = useContext(StudentContext);
  if (context === undefined) {
    throw new Error('useStudents must be used within a StudentProvider');
  }
  return context;
} 
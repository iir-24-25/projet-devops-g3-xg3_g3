import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Teacher } from '../types/navigation';

const STORAGE_KEY = '@teachers_data';

type TeacherContextType = {
  teachers: Teacher[];
  addTeacher: (teacher: Omit<Teacher, 'id'>) => Promise<void>;
  updateTeacher: (id: string, teacherData: Omit<Teacher, 'id'>) => Promise<void>;
  deleteTeacher: (id: string) => Promise<void>;
};

const TeacherContext = createContext<TeacherContextType | undefined>(undefined);

export function TeacherProvider({ children }: { children: React.ReactNode }) {
  const [teachers, setTeachers] = useState<Teacher[]>([]);

  useEffect(() => {
    loadTeachers();
  }, []);

  const loadTeachers = async () => {
    try {
      const storedTeachers = await AsyncStorage.getItem(STORAGE_KEY);
      if (storedTeachers) {
        setTeachers(JSON.parse(storedTeachers));
      } else {
        // Initialize with default data if no stored data exists
        const defaultTeachers: Teacher[] = [
          {
            id: '1',
            name: 'Sarah',
            familyName: 'Wilson',
            subject: 'Mathematics',
            group: 'Group A',
            role: 'teacher'
          },
          {
            id: '2',
            name: 'David',
            familyName: 'Brown',
            subject: 'Physics',
            group: 'Group B',
            role: 'teacher'
          },
          {
            id: '3',
            name: 'Emily',
            familyName: 'Davis',
            subject: 'English',
            group: 'Group C',
            role: 'teacher'
          }
        ];
        setTeachers(defaultTeachers);
        await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(defaultTeachers));
      }
    } catch (error) {
      console.error('Error loading teachers:', error);
    }
  };

  const saveTeachers = async (updatedTeachers: Teacher[]) => {
    try {
      // First update the state
      setTeachers(updatedTeachers);
      // Then save to AsyncStorage
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(updatedTeachers));
    } catch (error) {
      console.error('Error saving teachers:', error);
      // If saving fails, revert the state
      setTeachers(teachers);
      throw error;
    }
  };

  const addTeacher = async (teacherData: Omit<Teacher, 'id'>) => {
    try {
      const newTeacher: Teacher = {
        id: Date.now().toString(),
        ...teacherData,
      };
      await saveTeachers([...teachers, newTeacher]);
    } catch (error) {
      console.error('Error adding teacher:', error);
      throw error;
    }
  };

  const updateTeacher = async (id: string, teacherData: Omit<Teacher, 'id'>) => {
    try {
      const updatedTeachers = teachers.map((teacher) =>
        teacher.id === id ? { ...teacher, ...teacherData } : teacher
      );
      await saveTeachers(updatedTeachers);
    } catch (error) {
      console.error('Error updating teacher:', error);
      throw error;
    }
  };

  const deleteTeacher = async (id: string) => {
    try {
      const updatedTeachers = teachers.filter((teacher) => teacher.id !== id);
      await saveTeachers(updatedTeachers);
    } catch (error) {
      console.error('Error deleting teacher:', error);
      throw error;
    }
  };

  return (
    <TeacherContext.Provider value={{ teachers, addTeacher, updateTeacher, deleteTeacher }}>
      {children}
    </TeacherContext.Provider>
  );
}

export function useTeachers() {
  const context = useContext(TeacherContext);
  if (context === undefined) {
    throw new Error('useTeachers must be used within a TeacherProvider');
  }
  return context;
} 
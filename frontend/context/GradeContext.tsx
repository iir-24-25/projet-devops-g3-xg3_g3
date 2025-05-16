import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

export type Grade = {
  id: string;
  assignmentId: string;
  studentId: string;
  grade: number;
  feedback?: string;
  submittedAt: string;
  gradedAt: string;
};

type GradeContextType = {
  grades: Grade[];
  addGrade: (grade: Omit<Grade, 'id' | 'gradedAt'>) => Promise<void>;
  updateGrade: (id: string, grade: Partial<Grade>) => Promise<void>;
  deleteGrade: (id: string) => Promise<void>;
  getGradesByAssignment: (assignmentId: string) => Grade[];
  getGradesByStudent: (studentId: string) => Grade[];
  getGrade: (assignmentId: string, studentId: string) => Grade | undefined;
};

const GradeContext = createContext<GradeContextType | undefined>(undefined);

export function GradeProvider({ children }: { children: React.ReactNode }) {
  const [grades, setGrades] = useState<Grade[]>([]);

  useEffect(() => {
    loadGrades();
  }, []);

  const loadGrades = async () => {
    try {
      const savedGrades = await AsyncStorage.getItem('grades');
      if (savedGrades) {
        setGrades(JSON.parse(savedGrades));
      }
    } catch (error) {
      console.error('Error loading grades:', error);
    }
  };

  const saveGrades = async (newGrades: Grade[]) => {
    try {
      await AsyncStorage.setItem('grades', JSON.stringify(newGrades));
      setGrades(newGrades);
    } catch (error) {
      console.error('Error saving grades:', error);
    }
  };

  const addGrade = async (grade: Omit<Grade, 'id' | 'gradedAt'>) => {
    const newGrade: Grade = {
      ...grade,
      id: Date.now().toString(),
      gradedAt: new Date().toISOString(),
    };
    await saveGrades([...grades, newGrade]);
  };

  const updateGrade = async (id: string, updatedGrade: Partial<Grade>) => {
    const newGrades = grades.map(grade =>
      grade.id === id
        ? { ...grade, ...updatedGrade, gradedAt: new Date().toISOString() }
        : grade
    );
    await saveGrades(newGrades);
  };

  const deleteGrade = async (id: string) => {
    const newGrades = grades.filter(grade => grade.id !== id);
    await saveGrades(newGrades);
  };

  const getGradesByAssignment = (assignmentId: string) => {
    return grades.filter(grade => grade.assignmentId === assignmentId);
  };

  const getGradesByStudent = (studentId: string) => {
    return grades.filter(grade => grade.studentId === studentId);
  };

  const getGrade = (assignmentId: string, studentId: string) => {
    return grades.find(
      grade => grade.assignmentId === assignmentId && grade.studentId === studentId
    );
  };

  return (
    <GradeContext.Provider
      value={{
        grades,
        addGrade,
        updateGrade,
        deleteGrade,
        getGradesByAssignment,
        getGradesByStudent,
        getGrade,
      }}
    >
      {children}
    </GradeContext.Provider>
  );
}

export function useGrades() {
  const context = useContext(GradeContext);
  if (context === undefined) {
    throw new Error('useGrades must be used within a GradeProvider');
  }
  return context;
} 
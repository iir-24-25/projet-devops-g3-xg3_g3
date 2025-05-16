import React, { createContext, useContext } from 'react';
import { useAuth } from './AuthContext';

type RoleContextType = {
  isAdmin: boolean;
  isTeacher: boolean;
  isStudent: boolean;
  canAccessStudents: boolean;
  canAccessTeachers: boolean;
  canAccessGroups: boolean;
  canAccessAssignments: boolean;
  canCreateAssignment: boolean;
  canEditAssignment: boolean;
  canDeleteAssignment: boolean;
  canGradeAssignment: boolean;
};

const RoleContext = createContext<RoleContextType | undefined>(undefined);

export function RoleProvider({ children }: { children: React.ReactNode }) {
  const { user } = useAuth();

  const isAdmin = user?.userType === 'admin';
  const isTeacher = user?.userType === 'teacher';
  const isStudent = user?.userType === 'student';

  const canAccessStudents = isAdmin || isTeacher;
  const canAccessTeachers = isAdmin;
  const canAccessGroups = isAdmin || isTeacher;
  const canAccessAssignments = true; // All users can view assignments
  const canCreateAssignment = isAdmin || isTeacher;
  const canEditAssignment = isAdmin || isTeacher;
  const canDeleteAssignment = isAdmin || isTeacher;
  const canGradeAssignment = isAdmin || isTeacher;

  return (
    <RoleContext.Provider
      value={{
        isAdmin,
        isTeacher,
        isStudent,
        canAccessStudents,
        canAccessTeachers,
        canAccessGroups,
        canAccessAssignments,
        canCreateAssignment,
        canEditAssignment,
        canDeleteAssignment,
        canGradeAssignment,
      }}
    >
      {children}
    </RoleContext.Provider>
  );
}

export function useRole() {
  const context = useContext(RoleContext);
  if (context === undefined) {
    throw new Error('useRole must be used within a RoleProvider');
  }
  return context;
} 
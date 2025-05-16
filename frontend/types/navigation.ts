import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RouteProp } from '@react-navigation/native';

export type Student = {
  id: string;
  name: string;
  familyName: string;
  email: string;
  group: string;
  groupId: string;
  role: 'student' | 'teacher' | 'admin';
};

export type Teacher = {
  id: string;
  name: string;
  familyName: string;
  subject: string;
  group: string;
  role: 'teacher';
};

export type Group = {
  id: string;
  name: string;
  teacherId: string;
  studentIds: string[];
};

export type Assignment = {
  id: string;
  title: string;
  description: string;
  groupId: string;
  dueDate: string;
  fileUrl: string;
  fileName: string;
  createdAt: string;
  createdBy: string; // teacherId
  status: 'active' | 'completed' | 'cancelled';
};

export type Submission = {
  id: string;
  assignmentId: string;
  studentId: string;
  fileUrl: string;
  fileName: string;
  submittedAt: string;
  status: 'pending' | 'reviewed';
  feedback?: string;
  grade?: number;
};

export type User = {
  id: string;
  name: string;
  familyName: string;
  email: string;
  role: 'student' | 'teacher' | 'admin';
};

export type RootStackParamList = {
  Home: undefined;
  Login: undefined;
  Register: undefined;
  Dashboard: undefined;
  StudentList: undefined;
  AddStudent: undefined;
  StudentDetails: {
    studentId: string;
  };
  EditStudent: { studentId: string };
  TeacherList: undefined;
  AddTeacher: undefined;
  TeacherDetails: { teacherId: string };
  EditTeacher: { teacherId: string };
  GroupList: undefined;
  AddGroup: undefined;
  GroupDetails: { groupId: string };
  EditGroup: { groupId: string };
  AssignmentList: undefined;
  AddAssignment: undefined;
  AssignmentDetails: { assignmentId: string };
  EditAssignment: { assignmentId: string };
  GradeAssignment: { assignmentId: string };
  SubmissionList: { assignmentId: string };
  SubmissionDetails: { submissionId: string };
  ReviewSubmission: { submissionId: string };
};

export type NavigationProp = NativeStackNavigationProp<RootStackParamList>; 
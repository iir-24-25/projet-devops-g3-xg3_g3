import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { Toaster } from 'sonner-native';
import { AuthProvider } from './context/AuthContext';
import { StudentProvider } from './context/StudentContext';
import { TeacherProvider } from './context/TeacherContext';
import { GroupProvider } from './context/GroupContext';
import { AssignmentProvider } from './context/AssignmentContext';
import HomeScreen from './screens/HomeScreen';
import LoginScreen from './screens/LoginScreen';
import RegisterScreen from './screens/RegisterScreen';
import DashboardScreen from './screens/DashboardScreen';
import StudentListScreen from './screens/StudentListScreen';
import AddStudentScreen from './screens/AddStudentScreen';
import StudentDetailsScreen from './screens/StudentDetailsScreen';
import EditStudentScreen from './screens/EditStudentScreen';
import TeacherListScreen from './screens/TeacherListScreen';
import AddTeacherScreen from './screens/AddTeacherScreen';
import TeacherDetailsScreen from './screens/TeacherDetailsScreen';
import EditTeacherScreen from './screens/EditTeacherScreen';
import GroupListScreen from './screens/GroupListScreen';
import AddGroupScreen from './screens/AddGroupScreen';
import GroupDetailsScreen from './screens/GroupDetailsScreen';
import EditGroupScreen from './screens/EditGroupScreen';
import AssignmentListScreen from './screens/AssignmentListScreen';
import AddAssignmentScreen from './screens/AddAssignmentScreen';
import AssignmentDetailsScreen from './screens/AssignmentDetailsScreen';
import EditAssignmentScreen from './screens/EditAssignmentScreen';
import { RootStackParamList } from './types/navigation';

const Stack = createNativeStackNavigator<RootStackParamList>();

export default function App() {
  return (
    <SafeAreaProvider>
      <AuthProvider>
        <StudentProvider>
          <TeacherProvider>
            <GroupProvider>
              <AssignmentProvider>
                <NavigationContainer>
                  <Stack.Navigator
                    initialRouteName="Home"
                    screenOptions={{
                      headerShown: false,
                    }}
                  >
                    <Stack.Screen name="Home" component={HomeScreen} />
                    <Stack.Screen name="Login" component={LoginScreen} />
                    <Stack.Screen name="Register" component={RegisterScreen} />
                    <Stack.Screen name="Dashboard" component={DashboardScreen} />
                    <Stack.Screen name="StudentList" component={StudentListScreen} />
                    <Stack.Screen name="AddStudent" component={AddStudentScreen} />
                    <Stack.Screen name="StudentDetails" component={StudentDetailsScreen} />
                    <Stack.Screen name="EditStudent" component={EditStudentScreen} />
                    <Stack.Screen name="TeacherList" component={TeacherListScreen} />
                    <Stack.Screen name="AddTeacher" component={AddTeacherScreen} />
                    <Stack.Screen name="TeacherDetails" component={TeacherDetailsScreen} />
                    <Stack.Screen name="EditTeacher" component={EditTeacherScreen} />
                    <Stack.Screen name="GroupList" component={GroupListScreen} />
                    <Stack.Screen name="AddGroup" component={AddGroupScreen} />
                    <Stack.Screen name="GroupDetails" component={GroupDetailsScreen} />
                    <Stack.Screen name="EditGroup" component={EditGroupScreen} />
                    <Stack.Screen name="AssignmentList" component={AssignmentListScreen} />
                    <Stack.Screen name="AddAssignment" component={AddAssignmentScreen} />
                    <Stack.Screen name="AssignmentDetails" component={AssignmentDetailsScreen} />
                    <Stack.Screen name="EditAssignment" component={EditAssignmentScreen} />
                  </Stack.Navigator>
                </NavigationContainer>
                <Toaster />
              </AssignmentProvider>
            </GroupProvider>
          </TeacherProvider>
        </StudentProvider>
      </AuthProvider>
    </SafeAreaProvider>
  );
}
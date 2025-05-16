import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Modal,
} from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../types/navigation';
import { useStudents } from '../context/StudentContext';
import { useTeachers } from '../context/TeacherContext';
import { useGroups } from '../context/GroupContext';
import { useAssignments } from '../context/AssignmentContext';
import { useAuth } from '../context/AuthContext';

type DashboardScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'Dashboard'>;

export default function DashboardScreen() {
  const navigation = useNavigation<DashboardScreenNavigationProp>();
  const { students = [] } = useStudents();
  const { teachers = [] } = useTeachers();
  const { groups = [] } = useGroups();
  const { assignments = [] } = useAssignments();
  const { logout } = useAuth();
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);

  const handleNavigate = (screen: keyof RootStackParamList) => {
    try {
      setIsDrawerOpen(false);
      navigation.navigate(screen as any);
    } catch (error) {
      console.error('Navigation error:', error);
    }
  };

  const handleLogout = () => {
    setIsDrawerOpen(false);
    logout();
  };

  const renderDrawer = () => (
    <Modal
      visible={isDrawerOpen}
      transparent={true}
      animationType="slide"
      onRequestClose={() => setIsDrawerOpen(false)}
    >
      <View style={styles.modalOverlay}>
        <View style={styles.drawer}>
          <View style={styles.drawerHeader}>
            <Text style={styles.drawerTitle}>Menu</Text>
            <TouchableOpacity
              style={styles.closeButton}
              onPress={() => setIsDrawerOpen(false)}
            >
              <Text style={styles.closeButtonText}>×</Text>
            </TouchableOpacity>
          </View>
          <View style={styles.drawerContent}>
            <TouchableOpacity
              style={styles.drawerItem}
              onPress={() => handleNavigate('Dashboard')}
            >
              <Text style={styles.drawerItemText}>Dashboard</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.drawerItem}
              onPress={() => handleNavigate('StudentList')}
            >
              <Text style={styles.drawerItemText}>Students</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.drawerItem}
              onPress={() => handleNavigate('TeacherList')}
            >
              <Text style={styles.drawerItemText}>Teachers</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.drawerItem}
              onPress={() => handleNavigate('GroupList')}
            >
              <Text style={styles.drawerItemText}>Groups</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.drawerItem}
              onPress={() => handleNavigate('AssignmentList')}
            >
              <Text style={styles.drawerItemText}>Assignments</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.drawerItem, styles.logoutButton]}
              onPress={handleLogout}
            >
              <Text style={[styles.drawerItemText, styles.logoutText]}>Logout</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );

  return (
    <View style={styles.container}>
      {renderDrawer()}
      <ScrollView style={styles.mainContent}>
        <View style={styles.header}>
          <TouchableOpacity
            style={styles.menuButton}
            onPress={() => setIsDrawerOpen(true)}
          >
            <Text style={styles.menuButtonText}>☰</Text>
          </TouchableOpacity>
          <Text style={styles.title}>Dashboard</Text>
        </View>
        <View style={styles.content}>
          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Quick Stats</Text>
            <View style={styles.statsContainer}>
              <View style={styles.statCard}>
                <Text style={styles.statNumber}>{students?.length || 0}</Text>
                <Text style={styles.statLabel}>Students</Text>
              </View>
              <View style={styles.statCard}>
                <Text style={styles.statNumber}>{teachers?.length || 0}</Text>
                <Text style={styles.statLabel}>Teachers</Text>
              </View>
              <View style={styles.statCard}>
                <Text style={styles.statNumber}>{groups?.length || 0}</Text>
                <Text style={styles.statLabel}>Groups</Text>
              </View>
              <View style={styles.statCard}>
                <Text style={styles.statNumber}>{assignments?.length || 0}</Text>
                <Text style={styles.statLabel}>Assignments</Text>
              </View>
            </View>
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Quick Actions</Text>
            <View style={styles.actionsContainer}>
              <TouchableOpacity
                style={styles.actionCard}
                onPress={() => handleNavigate('AddStudent')}
              >
                <Text style={styles.actionTitle}>Add Student</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.actionCard}
                onPress={() => handleNavigate('AddTeacher')}
              >
                <Text style={styles.actionTitle}>Add Teacher</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.actionCard}
                onPress={() => handleNavigate('AddGroup')}
              >
                <Text style={styles.actionTitle}>Add Group</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.actionCard}
                onPress={() => handleNavigate('AddAssignment')}
              >
                <Text style={styles.actionTitle}>Add Assignment</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  mainContent: {
    flex: 1,
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  header: {
    padding: 16,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
    flexDirection: 'row',
    alignItems: 'center',
  },
  menuButton: {
    marginRight: 16,
  },
  menuButtonText: {
    fontSize: 24,
    color: '#1a73e8',
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#202124',
  },
  content: {
    padding: 16,
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: '600',
    color: '#202124',
    marginBottom: 16,
  },
  statsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 16,
  },
  statCard: {
    flex: 1,
    minWidth: '45%',
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  statNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#1a73e8',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 14,
    color: '#5f6368',
  },
  actionsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 16,
  },
  actionCard: {
    flex: 1,
    minWidth: '45%',
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  actionTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#1a73e8',
  },
  drawer: {
    position: 'absolute',
    left: 0,
    top: 0,
    bottom: 0,
    width: 300,
    backgroundColor: '#fff',
    shadowColor: '#000',
    shadowOffset: { width: 2, height: 0 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  drawerHeader: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  drawerTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#202124',
  },
  closeButton: {
    padding: 8,
  },
  closeButtonText: {
    fontSize: 24,
    color: '#666',
  },
  drawerContent: {
    padding: 16,
  },
  drawerItem: {
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  drawerItemText: {
    fontSize: 16,
    color: '#202124',
  },
  logoutButton: {
    marginTop: 16,
    borderBottomWidth: 0,
  },
  logoutText: {
    color: '#dc3545',
  },
}); 
import React, { createContext, useState, useContext, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { User } from '../types/navigation';

type AuthContextType = {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string, role: 'student' | 'teacher' | 'admin') => Promise<void>;
  logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const loadUser = async () => {
      try {
        const userJson = await AsyncStorage.getItem('user');
        if (userJson) {
          const loadedUser = JSON.parse(userJson) as User;
          console.log('Loaded user from storage:', loadedUser);
          setUser(loadedUser);
        }
      } catch (error) {
        console.error('Error loading user from storage:', error);
      }
    };

    loadUser();
  }, []);

  const login = async (email: string, password: string) => {
    try {
      // TODO: Implement actual login logic
      const mockUser: User = {
        id: '1',
        name: 'John',
        familyName: 'Doe',
        email: email,
        role: 'teacher' // Set a default role for testing
      };
      console.log('Setting user:', mockUser);
      setUser(mockUser);
      await AsyncStorage.setItem('user', JSON.stringify(mockUser));
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  };

  const register = async (name: string, email: string, password: string, role: 'student' | 'teacher' | 'admin') => {
    try {
      // TODO: Implement actual registration logic
      const mockUser: User = {
        id: '1',
        name: name.split(' ')[0],
        familyName: name.split(' ')[1] || '',
        email: email,
        role: role
      };
      console.log('Setting user:', mockUser);
      setUser(mockUser);
      await AsyncStorage.setItem('user', JSON.stringify(mockUser));
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  };

  const logout = async () => {
    try {
      setUser(null);
      await AsyncStorage.removeItem('user');
    } catch (error) {
      console.error('Logout error:', error);
      throw error;
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
} 
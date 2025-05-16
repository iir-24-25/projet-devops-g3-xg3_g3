import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Group } from '../types/navigation';

type GroupContextType = {
  groups: Group[];
  addGroup: (group: Omit<Group, 'id'>) => void;
  updateGroup: (id: string, group: Omit<Group, 'id'>) => void;
  deleteGroup: (id: string) => void;
  getGroupById: (id: string) => Group | undefined;
};

const GroupContext = createContext<GroupContextType | undefined>(undefined);

const STORAGE_KEY = '@groups';

export function GroupProvider({ children }: { children: React.ReactNode }) {
  const [groups, setGroups] = useState<Group[]>([]);

  useEffect(() => {
    loadGroups();
  }, []);

  const loadGroups = async () => {
    try {
      const storedGroups = await AsyncStorage.getItem(STORAGE_KEY);
      if (storedGroups) {
        setGroups(JSON.parse(storedGroups));
      }
    } catch (error) {
      console.error('Error loading groups:', error);
    }
  };

  const saveGroups = async (newGroups: Group[]) => {
    try {
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(newGroups));
    } catch (error) {
      console.error('Error saving groups:', error);
    }
  };

  const addGroup = (group: Omit<Group, 'id'>) => {
    const newGroup: Group = {
      ...group,
      id: Date.now().toString(),
    };
    const newGroups = [...groups, newGroup];
    setGroups(newGroups);
    saveGroups(newGroups);
  };

  const updateGroup = (id: string, group: Omit<Group, 'id'>) => {
    const newGroups = groups.map(g => 
      g.id === id ? { ...group, id } : g
    );
    setGroups(newGroups);
    saveGroups(newGroups);
  };

  const deleteGroup = (id: string) => {
    const newGroups = groups.filter(g => g.id !== id);
    setGroups(newGroups);
    saveGroups(newGroups);
  };

  const getGroupById = (id: string) => {
    return groups.find(g => g.id === id);
  };

  return (
    <GroupContext.Provider value={{
      groups,
      addGroup,
      updateGroup,
      deleteGroup,
      getGroupById,
    }}>
      {children}
    </GroupContext.Provider>
  );
}

export function useGroups() {
  const context = useContext(GroupContext);
  if (context === undefined) {
    throw new Error('useGroups must be used within a GroupProvider');
  }
  return context;
} 
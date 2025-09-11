import axios from 'axios';
import type {User} from '../app/type/DataType'

axios.defaults.baseURL = import.meta.env.VITE_API_URL; 
axios.defaults.withCredentials = true;                 


export async function fetchMe(): Promise<User | null> {
  try {
    const { data } = await axios.get<User>('/api/me');
    return data;
  } catch {
    return null;
  }
}
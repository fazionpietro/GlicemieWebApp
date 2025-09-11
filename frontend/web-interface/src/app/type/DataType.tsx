export type Paziente = {
  id: string;
  email: string;
  nome: string;
  cognome: string;
  dataNascita: string;        // ISO date "YYYY-MM-DD"
  ruolo: 'ROLE_PAZIENTE';
  fattoriRischio: string | null;
  comorbita: string | null;
  patologiePregresse: string | null;
  idMedico: string | null;
};




export interface User {
  id: string;
  email: string;
  role: string;
}

export interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (user: User) => void;
  logout: () => void;
  checkAuth: () => Promise<void>;
}
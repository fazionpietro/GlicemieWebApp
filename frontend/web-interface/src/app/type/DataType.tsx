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
  nomeMedico: string | null;
  cognomeMedico: string | null;
  emailMedico: string | null;
  passwordHash: any | null;
};

export type PazienteMedico = {

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
}


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


export type Medico = {
  id: string
  email: string
  nome: string
  cognome: string
  dataNascita: string
  ruolo: string
}

export type Terapia = {
  id: string,
  farmaco: string,
  numAssunzioni: number,
  dosaggio: string,
  indicazioni: string,
  idPaziente: string
  medicoCurante: string
}

export type Rilevazione = {

  id: string,
  idPaziente: string,
  valore: number,
  timestamp: string,
  livello: number
}

export type Assunzione = {
  id: string,
  idTerapia: string,
  latestTimestamp: string,
  giaAssunte: number
}

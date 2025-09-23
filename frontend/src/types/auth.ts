export interface LoginForm {
  username: string;
  password: string;
  remember?: boolean;
}

export interface User {
  id: string;
  username: string;
  name: string;
  email: string;
  department: string;
  position: string;
  role: 'admin' | 'hr' | 'employee';
}

export interface AuthResponse {
  success: boolean;
  token?: string;
  user?: User;
  message?: string;
}
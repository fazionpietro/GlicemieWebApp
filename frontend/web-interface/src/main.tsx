import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import Login from './app/LoginPage/Login'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css';
import Register from './app/RegisterPage/Register'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { HeaderMegaMenu } from './app/AdminPage/Header'
import AdminPage from './app/AdminPage/AdminPage'
import ProtectedRoute from './routes/ProtectedRoute'
import Unauthorized from './routes/Unhautorized'
import { AuthProvider } from './context/Authentication'

createRoot(document.getElementById('root')!).render(
   <StrictMode>
    <MantineProvider>
      <BrowserRouter>
      <AuthProvider>
         <Routes>
          
            <Route path='/login' element={<Login />} />
            <Route path='/register' element={<Register />} />
            <Route path='/admin2' element={<AdminPage/>}/>
            <Route path="/unauthorized" element={<Unauthorized />}/>
            <Route path='/admin' element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                <AdminPage />
              </ProtectedRoute>
            } />
        </Routes>
      </AuthProvider>
       
      </BrowserRouter>
    </MantineProvider>
  </StrictMode>
)

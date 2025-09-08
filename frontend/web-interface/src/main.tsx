import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import Login from './app/LoginPage/Login'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css';
import Register from './app/RegisterPage/Register'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AdminPage from './app/AdminPage/AdminPage'
import MedicPage from './app/MedicPage/MedicPage'
import UserPage from './app/UserPage/UserPage'

createRoot(document.getElementById('root')!).render(
   <StrictMode>
    <MantineProvider>
      <BrowserRouter>
        <Routes>
            <Route path='/' element={<Login />} />
            <Route path='/register' element={<Register />} />
            <Route path='/admin' element={<AdminPage />} />
            <Route path='/medic' element={<MedicPage/>}/>
            <Route path='/user' element={<UserPage/>}/>
        </Routes>
      </BrowserRouter>
    </MantineProvider>
  </StrictMode>
)

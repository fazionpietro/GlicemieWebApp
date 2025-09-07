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

createRoot(document.getElementById('root')!).render(
   <StrictMode>
    <MantineProvider>
      <BrowserRouter>
        <Routes>
            <Route path='/login' element={<Login />} />
            <Route path='/register' element={<Register />} />
            <Route path='/admin' element={<AdminPage />} />
        </Routes>
      </BrowserRouter>
    </MantineProvider>
  </StrictMode>
)

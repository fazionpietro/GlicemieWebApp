import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import Login from './app/LoginPage/Login'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css';

createRoot(document.getElementById('root')!).render(
   <StrictMode>
    <MantineProvider>
      <Login />
    </MantineProvider>
  </StrictMode>
)

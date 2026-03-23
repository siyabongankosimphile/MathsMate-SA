import { BrowserRouter, Routes, Route, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import Navbar from './components/Navbar'
import ProblemInput from './components/ProblemInput'
import SolutionDisplay from './components/SolutionDisplay'
import HistoryList from './components/HistoryList'
import CurriculumBrowser from './components/CurriculumBrowser'
import './index.css'

function SolvePage() {
  const [result, setResult] = useState(null)

  const handleSolution = ({ question, solution }) => {
    setResult({ question, solution })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div className="page">
      <header className="page-header">
        <h1>Smart Maths Solver</h1>
        <p className="subtitle">Aligned with the South African CAPS curriculum 🇿🇦</p>
      </header>
      <ProblemInput onSolution={handleSolution} />
      {result && <SolutionDisplay question={result.question} solution={result.solution} />}
      <div className="tips">
        <h3>Supported Problem Types</h3>
        <div className="tips-grid">
          <div className="tip"><strong>Arithmetic</strong><br />3 + 4 * (2 - 1)</div>
          <div className="tip"><strong>Linear Equations</strong><br />2x + 3 = 7</div>
          <div className="tip"><strong>Quadratic</strong><br />x&sup2; - 5x + 6 = 0</div>
          <div className="tip"><strong>Percentages</strong><br />25% of 200</div>
          <div className="tip"><strong>Trigonometry</strong><br />sin(30) &nbsp; cos(45)</div>
          <div className="tip"><strong>LCM / HCF</strong><br />LCM of 12 and 18</div>
          <div className="tip"><strong>Simple Interest</strong><br />principal=1000, rate=5, time=3</div>
        </div>
      </div>
    </div>
  )
}

function HistoryPage() {
  const navigate = useNavigate()
  return (
    <div className="page">
      <header className="page-header">
        <h1>Problem History</h1>
        <p className="subtitle">All problems you have submitted</p>
      </header>
      <HistoryList onSelect={q => navigate('/')} />
    </div>
  )
}

function CurriculumPage() {
  return (
    <div className="page">
      <header className="page-header">
        <h1>CAPS Curriculum</h1>
        <p className="subtitle">South African Mathematics curriculum topics by grade</p>
      </header>
      <CurriculumBrowser />
    </div>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<SolvePage />} />
          <Route path="/history" element={<HistoryPage />} />
          <Route path="/curriculum" element={<CurriculumPage />} />
          <Route path="*" element={<SolvePage />} />
        </Routes>
      </main>
      <footer className="app-footer">
        <p>MathsMate SA &mdash; Empowering South African learners &copy; {new Date().getFullYear()}</p>
      </footer>
    </BrowserRouter>
  )
}

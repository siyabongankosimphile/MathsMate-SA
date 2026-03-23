import { useState } from 'react'
import { solveText, solveImage } from '../api/mathsApi'

const GRADES = ['', 'Grade 7', 'Grade 8', 'Grade 9', 'Grade 10', 'Grade 11', 'Grade 12']
const TOPICS = ['', 'Algebra', 'Trigonometry', 'Number Operations', 'Finance Maths', 'Geometry', 'Statistics', 'Calculus']

export default function ProblemInput({ onSolution }) {
  const [question, setQuestion] = useState('')
  const [grade, setGrade] = useState('')
  const [topic, setTopic] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [tab, setTab] = useState('text') // 'text' | 'image'

  const handleTextSolve = async e => {
    e.preventDefault()
    if (!question.trim()) return
    setLoading(true)
    setError('')
    try {
      const solution = await solveText(question, grade, topic)
      onSolution({ question, solution })
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to get solution. Is the backend running?')
    } finally {
      setLoading(false)
    }
  }

  const handleImageSolve = async e => {
    const file = e.target.files[0]
    if (!file) return
    setLoading(true)
    setError('')
    const formData = new FormData()
    formData.append('file', file)
    try {
      const solution = await solveImage(formData)
      onSolution({ question: '(from image)', solution })
    } catch (err) {
      setError(err.response?.data?.message || 'Image upload failed.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card">
      <div className="tabs">
        <button className={`tab ${tab === 'text' ? 'active' : ''}`} onClick={() => setTab('text')}>
          Type Problem
        </button>
        <button className={`tab ${tab === 'image' ? 'active' : ''}`} onClick={() => setTab('image')}>
          Upload Image
        </button>
      </div>

      {tab === 'text' ? (
        <form onSubmit={handleTextSolve} className="problem-form">
          <textarea
            className="problem-textarea"
            placeholder="Enter your maths problem, e.g.&#10;2x + 3 = 7&#10;xÂ² - 5x + 6 = 0&#10;sin(30)&#10;25% of 200&#10;LCM of 12 and 18"
            value={question}
            onChange={e => setQuestion(e.target.value)}
            rows={5}
          />
          <div className="form-row">
            <select value={grade} onChange={e => setGrade(e.target.value)} className="select">
              {GRADES.map(g => <option key={g} value={g}>{g || 'Select Grade'}</option>)}
            </select>
            <select value={topic} onChange={e => setTopic(e.target.value)} className="select">
              {TOPICS.map(t => <option key={t} value={t}>{t || 'Select Topic'}</option>)}
            </select>
            <button type="submit" className="btn btn-primary" disabled={loading || !question.trim()}>
              {loading ? 'Solvingâ€¦' : 'Solve â–¶'}
            </button>
          </div>
        </form>
      ) : (
        <div className="image-upload-zone">
          <p>Upload a photo of your maths problem and we'll extract the text.</p>
          <label className="btn btn-secondary">
            Choose Image
            <input type="file" accept="image/*" onChange={handleImageSolve} hidden disabled={loading} />
          </label>
          {loading && <p className="loading-text">Processing imageâ€¦</p>}
        </div>
      )}

      {error && <p className="error-msg">{error}</p>}
    </div>
  )
}

import { useEffect, useState } from 'react'
import { getProblems, deleteProblem } from '../api/mathsApi'

export default function HistoryList({ onSelect }) {
  const [problems, setProblems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    load()
  }, [])

  const load = () => {
    setLoading(true)
    getProblems()
      .then(data => setProblems(data.reverse()))
      .catch(() => setError('Could not load history.'))
      .finally(() => setLoading(false))
  }

  const handleDelete = async (e, id) => {
    e.stopPropagation()
    try {
      await deleteProblem(id)
      setProblems(prev => prev.filter(p => p.id !== id))
    } catch {
      alert('Delete failed.')
    }
  }

  if (loading) return <p className="loading-text">Loading historyâ€¦</p>
  if (error) return <p className="error-msg">{error}</p>
  if (problems.length === 0) return <p className="empty-msg">No problems solved yet. <a href="/">Go solve one!</a></p>

  return (
    <div className="history-list">
      {problems.map(p => (
        <div
          key={p.id}
          className="history-item"
          onClick={() => onSelect && onSelect(p.question)}
        >
          <div className="history-question">{p.question}</div>
          <div className="history-meta">
            {p.topic && <span className="badge badge-topic">{p.topic}</span>}
            {p.grade && <span className="badge badge-grade">{p.grade}</span>}
            <span className="history-date">
              {p.createdAt ? new Date(p.createdAt).toLocaleDateString('en-ZA') : ''}
            </span>
          </div>
          <button
            className="btn btn-danger btn-sm"
            onClick={e => handleDelete(e, p.id)}
          >
            âœ•
          </button>
        </div>
      ))}
    </div>
  )
}

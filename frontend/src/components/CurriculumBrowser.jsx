import { useEffect, useState } from 'react'
import { getCurriculum } from '../api/mathsApi'

const GRADES = ['Grade 7', 'Grade 8', 'Grade 9', 'Grade 10', 'Grade 11', 'Grade 12']

export default function CurriculumBrowser() {
  const [allData, setAllData] = useState([])
  const [filtered, setFiltered] = useState([])
  const [selectedGrade, setSelectedGrade] = useState('')
  const [expanded, setExpanded] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    getCurriculum()
      .then(data => { setAllData(data); setFiltered(data) })
      .catch(() => setError('Could not load curriculum. Is the backend running?'))
      .finally(() => setLoading(false))
  }, [])

  const handleGradeFilter = grade => {
    setSelectedGrade(grade)
    setFiltered(grade ? allData.filter(c => c.grade === grade) : allData)
    setExpanded(null)
  }

  if (loading) return <p className="loading-text">Loading curriculumâ€¦</p>
  if (error) return <p className="error-msg">{error}</p>

  return (
    <div className="curriculum-browser">
      <div className="grade-filter">
        <button
          className={`btn btn-filter ${!selectedGrade ? 'active' : ''}`}
          onClick={() => handleGradeFilter('')}
        >All Grades</button>
        {GRADES.map(g => (
          <button
            key={g}
            className={`btn btn-filter ${selectedGrade === g ? 'active' : ''}`}
            onClick={() => handleGradeFilter(g)}
          >{g}</button>
        ))}
      </div>

      {filtered.length === 0 && <p className="empty-msg">No topics found for this selection.</p>}

      <div className="curriculum-list">
        {filtered.map(item => (
          <div key={item.id} className="curriculum-item">
            <div
              className="curriculum-header"
              onClick={() => setExpanded(expanded === item.id ? null : item.id)}
            >
              <div>
                <span className="curriculum-topic">{item.topic}</span>
                <span className="badge badge-grade">{item.grade}</span>
              </div>
              <span className="expand-icon">{expanded === item.id ? 'â–²' : 'â–¼'}</span>
            </div>
            {expanded === item.id && (
              <div className="curriculum-details">
                <p className="curriculum-description">{item.description}</p>
                {item.subtopics && item.subtopics.length > 0 && (
                  <>
                    <h4>Subtopics</h4>
                    <ul>
                      {item.subtopics.map((s, i) => <li key={i}>{s}</li>)}
                    </ul>
                  </>
                )}
                {item.capsReference && (
                  <p className="caps-ref"><em>{item.capsReference}</em></p>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}

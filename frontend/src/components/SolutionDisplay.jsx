export default function SolutionDisplay({ question, solution }) {
  if (!solution) return null

  return (
    <div className="card solution-card">
      <h2 className="solution-title">Solution</h2>

      <div className="solution-meta">
        {solution.topic && <span className="badge badge-topic">{solution.topic}</span>}
        {solution.method && <span className="badge badge-method">{solution.method}</span>}
        {solution.fromCache && <span className="badge badge-cache">Cached</span>}
      </div>

      {question && (
        <div className="solution-question">
          <strong>Problem:</strong> {question}
        </div>
      )}

      <div className="solution-answer">
        <span className="answer-label">Answer:</span>
        <span className="answer-value">{solution.answer}</span>
      </div>

      {solution.steps && solution.steps.length > 0 && (
        <div className="solution-steps">
          <h3>Step-by-Step</h3>
          <ol>
            {solution.steps.map((step, i) => (
              <li key={i} className="step-item">{step}</li>
            ))}
          </ol>
        </div>
      )}
    </div>
  )
}

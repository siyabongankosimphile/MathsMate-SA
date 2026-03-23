import { Link, NavLink } from 'react-router-dom'

export default function Navbar() {
  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">
        <span className="brand-icon">&Sigma;</span> MathsMate SA
      </Link>
      <ul className="nav-links">
        <li><NavLink to="/" end>Solve</NavLink></li>
        <li><NavLink to="/history">History</NavLink></li>
        <li><NavLink to="/curriculum">Curriculum</NavLink></li>
      </ul>
    </nav>
  )
}

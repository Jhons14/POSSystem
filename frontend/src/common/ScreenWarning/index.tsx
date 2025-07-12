import './index.css'

export function ScreenWarning({ warning }: { warning: string }): JSX.Element {
  return (
    <div className="Screen-warning">
      <h1>{warning}</h1>
    </div>
  )
}

import React from 'react';
import './index.css';

interface LoadingProps {
  size?: 'small' | 'medium' | 'large';
  text?: string;
  overlay?: boolean;
}

const Loading: React.FC<LoadingProps> = ({ 
  size = 'medium', 
  text = 'Loading...', 
  overlay = false 
}) => {
  const baseClass = 'loading-container';
  const sizeClass = `loading-${size}`;
  const overlayClass = overlay ? 'loading-overlay' : '';

  return (
    <div className={`${baseClass} ${sizeClass} ${overlayClass}`.trim()}>
      <div className="loading-spinner">
        <div className="spinner"></div>
      </div>
      {text && <p className="loading-text">{text}</p>}
    </div>
  );
};

export default Loading;
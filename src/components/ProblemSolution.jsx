import React, { useState } from 'react';
import CodeEditor from './CodeEditor';
import axios from 'axios';
import '../styles/CodeEditor.css';

const ProblemSolution = ({ problemId }) => {
  const [code, setCode] = useState('');
  const [output, setOutput] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    setLoading(true);
    try {
      const response = await axios.post(`http://localhost:8080/api/submissions/test/${problemId}`, {
        code: code,
        language: 'java'
      });
      setOutput(response.data);
    } catch (error) {
      setOutput('Error: ' + error.message);
    }
    setLoading(false);
  };

  return (
    <div className="problem-solution">
      <div className="editor-section">
        <CodeEditor 
          code={code} 
          onChange={(value) => setCode(value)}
          language="java"
        />
        <div className="button-group">
          <button 
            onClick={handleSubmit}
            disabled={loading}
            className="submit-button"
          >
            {loading ? 'Testing...' : 'Submit Solution'}
          </button>
        </div>
      </div>
      
      {output && (
        <div className="output-section">
          <h3>Output</h3>
          <pre className="output-display">
            {output}
          </pre>
        </div>
      )}
    </div>
  );
};

export default ProblemSolution; 
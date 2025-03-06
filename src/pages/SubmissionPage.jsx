import React, { useState } from 'react';
import CodeEditor from '../components/CodeEditor';
import axios from 'axios';

const SubmissionPage = () => {
  const [code, setCode] = useState('');
  const [output, setOutput] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/api/submissions/execute', {
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
    <div className="submission-page">
      <div className="editor-section">
        <h2>Code Editor</h2>
        <CodeEditor 
          code={code} 
          onChange={(value) => setCode(value)}
          language="java"
        />
        <button 
          onClick={handleSubmit}
          disabled={loading}
          className="submit-button"
        >
          {loading ? 'Running...' : 'Run Code'}
        </button>
      </div>
      
      <div className="output-section">
        <h2>Output</h2>
        <pre className="output-display">
          {output}
        </pre>
      </div>
    </div>
  );
};

export default SubmissionPage; 
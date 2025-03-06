import React, { useState } from 'react';
import CodeEditor from '../components/CodeEditor';

const ProblemPage = () => {
  const [code, setCode] = useState('');
  const [output, setOutput] = useState('');

  const handleSubmit = async () => {
    try {
      const response = await fetch('/api/execute', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          language: 'java',
          code: code
        }),
      });
      const result = await response.json();
      setOutput(result.output || result.error);
    } catch (error) {
      setOutput('Error executing code');
    }
  };

  return (
    <div className="problem-page">
      <div className="editor-section">
        <CodeEditor 
          code={code} 
          onChange={(value) => setCode(value)} 
        />
        <button onClick={handleSubmit}>Run Code</button>
      </div>
      <div className="output-section">
        <pre>{output}</pre>
      </div>
    </div>
  );
};

export default ProblemPage; 
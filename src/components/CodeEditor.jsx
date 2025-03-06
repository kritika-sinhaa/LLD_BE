import React, { useRef } from 'react';
import Editor from '@monaco-editor/react';

const CodeEditor = ({ code, onChange, language = 'java' }) => {
  const editorRef = useRef(null);

  const handleEditorDidMount = (editor) => {
    editorRef.current = editor;
  };

  const defaultCode = `public class Solution {
    public static void main(String[] args) {
        // Write your code here
        
    }
}`;

  return (
    <div className="code-editor-container" style={{ height: '500px', border: '1px solid #ccc' }}>
      <Editor
        height="100%"
        defaultLanguage={language}
        value={code || defaultCode}
        onChange={onChange}
        onMount={handleEditorDidMount}
        theme="vs-dark"
        options={{
          minimap: { enabled: false },
          fontSize: 14,
          scrollBeyondLastLine: false,
          automaticLayout: true,
          lineNumbers: 'on',
          wordWrap: 'on',
          formatOnPaste: true,
          formatOnType: true
        }}
      />
    </div>
  );
};

export default CodeEditor; 
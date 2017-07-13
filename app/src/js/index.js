require('../less/pages/app.less');

import React from 'react';
import ReactDOM from 'react-dom';
import ChatApp from './components/chat/ChatApp.jsx';

ReactDOM.render(<ChatApp />, document.getElementById('chat-container'));
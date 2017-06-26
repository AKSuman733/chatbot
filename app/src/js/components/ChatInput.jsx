import React from 'react';
import * as Constants from './Constants.jsx';

class ChatInput extends React.Component {

    constructor(props) {
        super(props);
        this.state = {chatInput: '', sendDisabled: true, inputMargin: 60, isFlyoutOpen: false};

        this.submitHandler = this.submitHandler.bind(this);
        this.textChangeHandler = this.textChangeHandler.bind(this);
        this.inputFocus = this.inputFocus.bind(this);
        this.inputBlur = this.inputBlur.bind(this);
        this.toggleFlyout = this.toggleFlyout.bind(this);
        this.onFlyoutOptionClick = this.onFlyoutOptionClick.bind(this);
    }

    submitHandler(event) {
        event.preventDefault();
        if(this.state.chatInput != '') {
            this.setState({chatInput: '', sendDisabled: true});
            this.props.onSend({
                messageType: Constants.request.RequestType.TEXT_MESSAGE,
                messageData: [{text: this.state.chatInput}]
            });
        }
    }

    toggleFlyout() {
        this.setState({isFlyoutOpen: !this.state.isFlyoutOpen});
    }

    inputFocus() {
        this.setState({inputMargin: 0, isFlyoutOpen: false});
    }

    inputBlur() {
        this.setState({inputMargin: 60});
    }

    textChangeHandler(event) {
        let value = event.target.value;
        this.setState({chatInput: value, sendDisabled: value == ''});
    }

    onFlyoutOptionClick(event, type) {
        this.setState({isFlyoutOpen: false});
        switch(type) {
            case 'feedback':
                this.props.showFeedback();
            break;
        }
    }

    render() {
        let sendButtonClass = 'btn btn-fab btn-raised send-button' + (this.state.sendDisabled ? ' disabled' : '');
        let moreButtonClass = 'btn btn-fab btn-raised more-button';
        let moreButtonOptionClass = 'btn btn-raised btn-fab btn-fab-mini';
        let flyoutClass = 'more-button-container' + (this.state.isFlyoutOpen ? ' active' : '');

        return (
            <form onSubmit={this.submitHandler} id="chat-input-container">
                <fieldset>
                    <div className="form-group">
                        {this.state.inputMargin > 0 && (
                            <nav className={flyoutClass}>
                                <a onClick={(event) => this.onFlyoutOptionClick(event, 'feedback')} className="button">
                                    <div className={`${moreButtonOptionClass} btn-danger`}>
                                        <img src="/images/icon-feedback-32.png" />
                                    </div>
                                    <span>Feedback</span>
                                </a>
                                <a className={moreButtonClass} onClick={this.toggleFlyout}>
                                    <img src="/images/icon-plus-32.png" />
                                </a>
                            </nav>
                        )}

                        <input type="text"
                               className="form-control"
                               id="chat-input"
                               onChange={this.textChangeHandler}
                               value={this.state.chatInput}
                               placeholder="Write a message"
                               autoComplete="off"
                               style={{marginLeft: this.state.inputMargin}}
                               onFocus={this.inputFocus}
                               onBlur={this.inputBlur}
                               />
                        <a href="#" onClick={this.submitHandler} className={sendButtonClass}>
                             <img src="/images/icon-send-32.png" />
                        </a>
                    </div>
                </fieldset>
            </form>
        );
    }
}

ChatInput.defaultProps = {

};

export default ChatInput;
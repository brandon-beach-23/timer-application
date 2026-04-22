import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
    selector: 'app-websocket-test',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div style="padding: 20px; font-family: sans-serif;">
      <h2>WebSocket Test Connection</h2>
      <p>Status: <strong>{{ status }}</strong></p>
      <button (click)="connect()">Connect</button>
      <button (click)="sendMessage()" [disabled]="status !== 'Connected'">Send Test Message</button>
      <ul>
        <li *ngFor="let msg of messages">{{ msg }}</li>
      </ul>
    </div>
  `
})
export class WebSocketTestComponent implements OnInit {
    status = 'Disconnected';
    messages: string[] = [];
    private stompClient = null;

    ngOnInit(){}

    connect(){
        const socket = new SockJS('http://localhost:8080/ws/timer');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, (frame) => {
            this.status = 'Connected';
            this.stompClient.subscribe('/topic', (message) => {
                this.messages.push(JSON.parse(message.body));
            });
        }, (error) => {
            this.status = 'Error ' + error.message;
        });
    }

    sendMessage(){
        this.stompClient.subscribe('/app/test', {}, JSON.stringify("Hello from Angular!"));
    }
}
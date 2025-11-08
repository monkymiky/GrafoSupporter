export enum MessageType {
  error = 0,
  warning = 1,
  info = 2,
  success = 3,
}

export interface Message {
  id: number;
  text: string;
  type: MessageType;
}

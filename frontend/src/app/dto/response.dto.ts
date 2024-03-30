import { OperationStatus } from '../constants/status.enum';

export interface AppResponse {
  status: OperationStatus;
  message: string | null;
  data?: any;
}

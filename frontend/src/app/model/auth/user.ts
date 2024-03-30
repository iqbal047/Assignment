import { BaseModel } from '../super-model/base-model';
import { Role } from './role';

export interface User extends BaseModel {
  name?: string;
  username?: string;
  password?: string;
  roles?: Array<Role>;
}

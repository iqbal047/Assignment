export interface LoginResponse {
  username?: string ;
  roles?: Array<string> ;
  permissions?: Array<string> ;
  accessToken?: string ;
  status?: string ;
  message?: string ;
}

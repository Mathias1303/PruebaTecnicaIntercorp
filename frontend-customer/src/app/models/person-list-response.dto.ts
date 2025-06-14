import { CustomerDto } from "./customer.dto";

// Esta interfaz define la estructura completa de la respuesta de la API /list
export interface PersonListResponseDto {
  lastUpdated: Date;
  data: CustomerDto[];
}
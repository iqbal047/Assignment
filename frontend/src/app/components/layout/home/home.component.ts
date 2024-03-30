import { Component } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  selectedFile: File | null = null;

  constructor(private productService: ProductService, private authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }

  onFileSelected(event: any): void {
    this.selectedFile = <File>event.target.files[0];
  }

  onUpload(): void {
    console.log('file', this.selectedFile);
    if (this.selectedFile) {
      this.productService.uploadFile(this.selectedFile).subscribe(
        (response) => {
          console.log('File uploaded successfully');
        },
        (error) => {
          console.error('Failed to upload the file');
        }
      );
    }
  }

  onDownload(): void {
    this.productService.downloadCSV().subscribe(
      (data: Blob) => {
        const blob = new Blob([data], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      },
      (error) => {
        console.error('Failed to download the file');
      }
    );
  }
}

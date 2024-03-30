import {Component, OnInit} from '@angular/core';
import {IFood} from '../../model/i-food';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {FoodService} from '../../service/food.service';
import {CartDto} from '../../dto/cart-dto';
import Swal from 'sweetalert2';
import {TokenStorageService} from '../../service/token-storage.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  food: IFood;
  username: string;

  constructor(private foodService: FoodService,
              private activatedRoute: ActivatedRoute,
              private tokenService: TokenStorageService,
              private title: Title,
              private router: Router) {
  }

  ngOnInit(): void {
    this.title.setTitle('Chi tiết sản phẩm');
    const id = Number(this.activatedRoute.snapshot.params.id);
    this.foodService.getFoodById(id).subscribe(value => {
      console.log(value);
      this.food = value;
    });
    this.getCustomer();
  }

  getCustomer(): void {
    this.username = this.tokenService.getUser().username;
  }

  addToCart(item: CartDto) {
    if (this.username == null) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: 'Bạn chưa đăng nhập, vui lòng đăng nhập trước !',
        showConfirmButton: false,
        timer: 2000
      });
      this.router.navigateByUrl('/login');
    } else {
      this.foodService.updateCart(item, this.username).subscribe(() => {
        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          timer: 3000,
          timerProgressBar: true
        });
        Toast.fire('Thêm Vào Giỏ Hàng Thành Công', '', 'success');
      });
    }
  }
}

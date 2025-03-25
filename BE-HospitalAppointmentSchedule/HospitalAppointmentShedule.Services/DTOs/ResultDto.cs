using System.Collections.Generic;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class ResultDto<T>
    {
        public bool IsSuccess { get; set; }
        public string Message { get; set; } = string.Empty;
        public T? Data { get; set; }
        public List<string> Errors { get; set; } = new List<string>();

        public static ResultDto<T> Success(T data, string message = "")
        {
            return new ResultDto<T>
            {
                IsSuccess = true,
                Message = message,
                Data = data
            };
        }

        public static ResultDto<T> Failure(string message, List<string>? errors = null)
        {
            return new ResultDto<T>
            {
                IsSuccess = false,
                Message = message,
                Errors = errors ?? new List<string>()
            };
        }
    }

    public class PaginatedResultDto<T>
    {
        public int PageIndex { get; set; }
        public int PageSize { get; set; }
        public int TotalCount { get; set; }
        public int TotalPages { get; set; }
        public bool HasPreviousPage { get; set; }
        public bool HasNextPage { get; set; }
        public List<T> Items { get; set; } = new List<T>();

        public static PaginatedResultDto<T> Create(
            List<T> items,
            int totalCount,
            int pageIndex,
            int pageSize)
        {
            var totalPages = (int)System.Math.Ceiling(totalCount / (double)pageSize);
            return new PaginatedResultDto<T>
            {
                PageIndex = pageIndex,
                PageSize = pageSize,
                TotalCount = totalCount,
                TotalPages = totalPages,
                HasPreviousPage = pageIndex > 1,
                HasNextPage = pageIndex < totalPages,
                Items = items
            };
        }
    }
} 
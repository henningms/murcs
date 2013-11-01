using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Murcs.Models
{
    public class User
    {
        public long Id { get; set; }
        public string UserId { get; set; }
        public string Status { get; set; }
        public string UserName { get; set; }
        public string Image { get; set; }
    }
}
